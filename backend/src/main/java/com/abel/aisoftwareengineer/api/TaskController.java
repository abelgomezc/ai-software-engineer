package com.abel.aisoftwareengineer.api;

import com.abel.aisoftwareengineer.agent.AgentService;
import com.abel.aisoftwareengineer.domain.TaskRun;
import com.abel.aisoftwareengineer.repo.TaskRunRepository;
import com.abel.aisoftwareengineer.repo.RunStepRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRunRepository taskRepo;
    private final RunStepRepository stepRepo;
    private final AgentService agent;
    private final ExecutorService exec = Executors.newCachedThreadPool();

    public TaskController(TaskRunRepository taskRepo, RunStepRepository stepRepo, AgentService agent){
        this.taskRepo=taskRepo; this.stepRepo=stepRepo; this.agent=agent;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String,String> body){
        TaskRun tr = new TaskRun();
        tr.setRepoSource(body.get("repo_source"));
        tr.setTaskDescription(body.get("task_description"));
        tr.setStatus("PENDING");
        taskRepo.save(tr);
        Long id=tr.getId();
        exec.submit(()->{ try{ agent.run(id, tr.getRepoSource(), tr.getTaskDescription()); }catch(Exception e){ tr.setStatus("FAILED"); tr.setSummary(e.getMessage()); taskRepo.save(tr);} });
        return ResponseEntity.accepted().body(Map.of("id",id));
    }

    @GetMapping
    public Object list(){ return taskRepo.findAll(); }

    @GetMapping("/{id}")
    public Object get(@PathVariable Long id){ return taskRepo.findById(id).orElse(null); }

    @GetMapping("/{id}/steps")
    public Object steps(@PathVariable Long id){ return stepRepo.findByTaskRunIdOrderByStepNumberAsc(id); }

    @GetMapping("/{id}/events")
    public SseEmitter events(@PathVariable Long id){
        SseEmitter e = new SseEmitter(0L);
        return e;
    }

    @PostMapping("/{id}/cancel")
    public Object cancel(@PathVariable Long id){
        TaskRun tr = taskRepo.findById(id).orElse(null);
        if(tr!=null){ tr.setStatus("CANCELLED"); taskRepo.save(tr);}
        return Map.of("status","cancelled");
    }
}
