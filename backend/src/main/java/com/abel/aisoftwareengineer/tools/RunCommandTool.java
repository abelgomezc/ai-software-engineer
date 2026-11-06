package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.abel.aisoftwareengineer.sandbox.SandboxManager;

public class RunCommandTool implements Tool {
    private final String root; private final AiseProperties props;
    private final SandboxManager sandbox; private final String containerId;
    public RunCommandTool(String root, AiseProperties props, SandboxManager sandbox, String containerId){this.root=root;this.props=props;this.sandbox=sandbox;this.containerId=containerId;}
    public String name(){return "run_command";}
    public String description(){return "Run command in sandbox";}
    public String execute(String inputJson){
        try{
            String cmd=ReadFileTool.extract(inputJson,"command");
            String first=cmd.trim().split("\\s+")[0];
            if(!java.util.Arrays.asList(props.sandbox.allowedCommands.split(",")).contains(first)){
                return "ERROR: command not allowed: "+first;
            }
            return sandbox.exec(containerId, "cd /work && "+cmd, props.limits.commandTimeoutSeconds);
        }catch(Exception e){return "ERROR: "+e.getMessage();}
    }
}
