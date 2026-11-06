package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;

public class FinishTool implements Tool {
    private final AiseProperties props;
    public volatile String summary = "";
    public FinishTool(AiseProperties props){this.props=props;}
    public String name(){return "finish";}
    public String description(){return "Declare task finished with summary";}
    public String execute(String inputJson){ summary=ReadFileTool.extract(inputJson,"summary"); return "FINISHED"; }
}
