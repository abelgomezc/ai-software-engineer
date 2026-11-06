package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import java.nio.file.*;

public class EditFileTool implements Tool {
    private final String root; private final AiseProperties props;
    public EditFileTool(String root, AiseProperties props){this.root=root;this.props=props;}
    public String name(){return "edit_file";}
    public String description(){return "Replace exact fragment old_str with new_str";}
    public String execute(String inputJson){
        try{
            String path=ReadFileTool.extract(inputJson,"path");
            String oldStr=ReadFileTool.extract(inputJson,"old_str");
            String newStr=ReadFileTool.extract(inputJson,"new_str");
            Path p=PathGuard.resolve(root,path);
            String content=Files.readString(p);
            int idx=content.indexOf(oldStr);
            if(idx<0) return "ERROR: fragment not found";
            if(content.indexOf(oldStr,idx+1)>=0) return "ERROR: fragment not unique";
            content=content.replace(oldStr,newStr);
            Files.writeString(p,content);
            return "OK: edited "+path;
        }catch(Exception e){return "ERROR: "+e.getMessage();}
    }
}
