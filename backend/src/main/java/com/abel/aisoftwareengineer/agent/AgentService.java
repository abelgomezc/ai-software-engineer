package com.abel.aisoftwareengineer.agent;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.abel.aisoftwareengineer.domain.RunStep;
import com.abel.aisoftwareengineer.domain.TaskRun;
import com.abel.aisoftwareengineer.repo.RunStepRepository;
import com.abel.aisoftwareengineer.repo.TaskRunRepository;
import com.abel.aisoftwareengineer.sandbox.SandboxManager;
import com.abel.aisoftwareengineer.tools.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Service
public class AgentService {
    private final AiseProperties props;
    private final ChatModel chatModel;
    private final TaskRunRepository taskRepo;
    private final RunStepRepository stepRepo;
    private final SandboxManager sandbox;
    private final com.abel.aisoftwareengineer.repo.RepoManager repoManager;

    public AgentService(AiseProperties props, ChatModel chatModel, TaskRunRepository taskRepo, RunStepRepository stepRepo, SandboxManager sandbox, com.abel.aisoftwareengineer.repo.RepoManager repoManager){
        this.props=props; this.chatModel=chatModel; this.taskRepo=taskRepo; this.stepRepo=stepRepo; this.sandbox=sandbox; this.repoManager=repoManager;
    }

    public volatile boolean cancelled = false;

    public void run(Long taskId, String repoSource, String taskDesc) throws Exception {
        TaskRun tr = taskRepo.findById(taskId).orElseThrow();
        tr.setStatus("RUNNING"); taskRepo.save(tr);
        Path workRoot = Path.of("work", String.valueOf(taskId));
        Path workDir = repoManager.prepareWorkCopy(repoSource, workRoot);
        String containerId = sandbox.createContainer(workDir.toAbsolutePath().toString(), "eclipse-temurin:21-jdk");
        AgentTools agentTools = new AgentTools(workDir.toString(), props, sandbox, containerId, repoManager);
        ChatClient client = ChatClient.builder(chatModel).build();
        String system = "You are a junior software engineer. Use tools to explore, edit, test, and finish. When done call finish with summary.";
        List<String> history = new ArrayList<>();
        history.add("Task: " + taskDesc);
        Map<String,Integer> loopCount = new HashMap<>();
        int step=0;
        Instant start = Instant.now();
        try {
            for(int iter=0; iter<props.limits.maxIterations; iter++){
                if(cancelled){ tr.setStatus("CANCELLED"); taskRepo.save(tr); return; }
                if(Instant.now().isAfter(start.plusSeconds(props.limits.taskTimeoutSeconds))){
                    tr.setStatus("TIMED_OUT"); taskRepo.save(tr); return;
                }
                String userMsg = String.join("\n", history);
                ExecutorService ex = Executors.newSingleThreadExecutor();
                Future<String> f = ex.submit(() -> client.prompt().system(system).user(userMsg).tools(agentTools).call().content());
                String response;
                try { response = f.get(props.llm.timeoutSeconds, TimeUnit.SECONDS); }
                catch(TimeoutException te){ f.cancel(true); tr.setStatus("TIMED_OUT"); taskRepo.save(tr); return; }
                finally { ex.shutdownNow(); }
                history.add("ASSISTANT: "+response);
                step++;
                RunStep rs = new RunStep();
                rs.setTaskRunId(taskId); rs.setStepNumber(step); rs.setRole("engineer");
                rs.setThoughtSummary(response); rs.setStatus("OK"); rs.setDurationMs(0L);
                stepRepo.save(rs);
                if(agentTools.finishTool.summary.length()>0) break;
                String key = response;
                loopCount.put(key, loopCount.getOrDefault(key,0)+1);
                if(loopCount.get(key)>=props.limits.loopDetectionRepeat){
                    tr.setStatus("FAILED"); tr.setSummary("loop detected"); taskRepo.save(tr); return;
                }
            }
            tr.setStatus("SUCCEEDED");
            tr.setSummary(agentTools.finishTool.summary);
            tr.setFinalDiff(repoManager.diff(workDir));
            tr.setFinishedAt(Instant.now());
            taskRepo.save(tr);
        } finally {
            sandbox.remove(containerId);
        }
    }
}
