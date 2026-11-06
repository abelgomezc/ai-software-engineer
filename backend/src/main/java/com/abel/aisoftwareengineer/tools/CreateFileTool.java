package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import java.nio.file.*;

public class CreateFileTool implements Tool {
    private final String root; private final AiseProperties props;
    public CreateFileTool(String root, AiseProperties props){this.root=root;this.props=props;}
    public String name(){return "create_file";}
    public String description(){return "Create a new file";}
    public String execute(String inputJson){
        try{
            String path=ReadFileTool.extract(inputJson,"path");
            String content=ReadFileTool.extract(inputJson,"content");
            Path p=PathGuard.resolve(root,path);
            if(Files.exists(p)) return "ERROR: file already exists";
            Files.createDirectories(p.getParent());
            Files.writeString(p,content);
            return "OK: created "+path;
        }catch(Exception e){return "ERROR: "+e.getMessage();}
    }
}
