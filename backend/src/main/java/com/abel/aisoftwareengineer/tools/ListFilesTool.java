package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import java.nio.file.*;
import java.util.stream.*;

public class ListFilesTool implements Tool {
    private final String root; private final AiseProperties props;
    public ListFilesTool(String root, AiseProperties props){this.root=root;this.props=props;}
    public String name(){return "list_files";}
    public String description(){return "List repo structure";}
    public String execute(String inputJson){
        try{
            Path r=Paths.get(root);
            StringBuilder sb=new StringBuilder();
            Files.walk(r).filter(Files::isRegularFile).limit(500).forEach(p->{
                String rel=r.relativize(p).toString();
                if(rel.contains("target/")||rel.contains("node_modules/")||rel.contains(".git/")) return;
                sb.append(rel).append("\n");
            });
            String out=sb.toString();
            if(out.length()>props.limits.toolOutputMaxChars) out=out.substring(0,props.limits.toolOutputMaxChars)+"\n...[truncated]";
            return out;
        }catch(Exception e){return "ERROR: "+e.getMessage();}
    }
}
