package com.abel.aisoftwareengineer.agent;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.abel.aisoftwareengineer.sandbox.SandboxManager;
import com.abel.aisoftwareengineer.tools.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class AgentTools {
    private final String root;
    private final AiseProperties props;
    private final SandboxManager sandbox;
    private final String containerId;
    private final com.abel.aisoftwareengineer.repo.RepoManager repo;
    public final FinishTool finishTool;

    public AgentTools(String root, AiseProperties props, SandboxManager sandbox, String containerId, com.abel.aisoftwareengineer.repo.RepoManager repo){
        this.root=root; this.props=props; this.sandbox=sandbox; this.containerId=containerId; this.repo=repo;
        this.finishTool=new FinishTool(props);
    }

    @Tool(description="List repo files")
    public String list_files(){ return new ListFilesTool(root,props).execute("{}"); }

    @Tool(description="Read file with optional start/end lines")
    public String read_file(@ToolParam(description="path") String path,
                            @ToolParam(description="start line") Integer start,
                            @ToolParam(description="end line") Integer end){
        String json="{\"path\":\""+path+"\",\"start\":"+(start==null?1:start)+",\"end\":"+(end==null?100000:end)+"}";
        return new ReadFileTool(root,props).execute(json);
    }

    @Tool(description="Search code text")
    public String search_code(@ToolParam(description="query") String query){
        return new SearchCodeTool(root,props).execute("{\"query\":\""+query+"\"}");
    }

    @Tool(description="Edit file replacing old_str with new_str")
    public String edit_file(@ToolParam(description="path") String path,
                            @ToolParam(description="old_str") String old_str,
                            @ToolParam(description="new_str") String new_str){
        return new EditFileTool(root,props).execute("{\"path\":\""+path+"\",\"old_str\":\""+escape(old_str)+"\",\"new_str\":\""+escape(new_str)+"\"}");
    }

    @Tool(description="Create new file")
    public String create_file(@ToolParam(description="path") String path,
                              @ToolParam(description="content") String content){
        return new CreateFileTool(root,props).execute("{\"path\":\""+path+"\",\"content\":\""+escape(content)+"\"}");
    }

    @Tool(description="Run command in sandbox")
    public String run_command(@ToolParam(description="command") String command){
        return new RunCommandTool(root,props,sandbox,containerId).execute("{\"command\":\""+escape(command)+"\"}");
    }

    @Tool(description="Show git diff")
    public String git_diff(){ return new GitDiffTool(root,props,repo).execute("{}"); }

    @Tool(description="Finish task with summary")
    public String finish(@ToolParam(description="summary") String summary){
        return finishTool.execute("{\"summary\":\""+escape(summary)+"\"}");
    }

    private String escape(String s){ return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n"); }
}
