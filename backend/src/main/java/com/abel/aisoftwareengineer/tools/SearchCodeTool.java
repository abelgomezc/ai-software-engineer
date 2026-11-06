package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import java.nio.file.*;
import java.util.stream.*;

public class SearchCodeTool implements Tool {
    private final String root; private final AiseProperties props;
    public SearchCodeTool(String root, AiseProperties props){this.root=root;this.props=props;}
    public String name(){return "search_code";}
    public String description(){return "Grep-like text search";}
    public String execute(String inputJson){
        try{
            String q=ReadFileTool.extract(inputJson,"query");
            Path r=Paths.get(root);
            StringBuilder sb=new StringBuilder(); int count=0;
            try(Stream<Path> s=Files.walk(r)){
                for(Path p: (Iterable<Path>)s.filter(Files::isRegularFile)::iterator){
                    String rel=r.relativize(p).toString();
                    if(rel.contains("target/")||rel.contains("node_modules/")||rel.contains(".git/")) continue;
                    var lines=Files.readAllLines(p);
                    for(int i=0;i<lines.size();i++){
                        if(lines.get(i).contains(q)){ sb.append(rel).append(":").append(i+1).append(": ").append(lines.get(i)).append("\n"); if(++count>100) break;}
                    }
                    if(count>100) break;
                }
            }
            String out=sb.toString();
            if(out.length()>props.limits.toolOutputMaxChars) out=out.substring(0,props.limits.toolOutputMaxChars)+"\n...[truncated]";
            return out.isEmpty()?"No matches":out;
        }catch(Exception e){return "ERROR: "+e.getMessage();}
    }
}
