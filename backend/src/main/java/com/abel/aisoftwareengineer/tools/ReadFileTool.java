package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFileTool implements Tool {
    private final String root; private final AiseProperties props;
    public ReadFileTool(String root, AiseProperties props){ this.root=root; this.props=props; }
    public String name(){ return "read_file"; }
    public String description(){ return "Read a file with optional line range"; }
    public String execute(String inputJson){
        try {
            // parse minimal: {"path":"...","start":1,"end":100}
            String path = extract(inputJson,"path");
            int start = intVal(inputJson,"start",1);
            int end = intVal(inputJson,"end",Integer.MAX_VALUE);
            Path p = PathGuard.resolve(root,path);
            var lines = Files.readAllLines(p);
            StringBuilder sb=new StringBuilder();
            int s=Math.max(1,start), e=Math.min(lines.size(),end);
            for(int i=s-1;i<e;i++) sb.append((i+1)).append(": ").append(lines.get(i)).append("\n");
            String out=sb.toString();
            if(out.length()>props.limits.toolOutputMaxChars) out=out.substring(0,props.limits.toolOutputMaxChars)+"\n...[truncated]";
            return out.isEmpty()?"(empty)":out;
        } catch(Exception e){ return "ERROR: "+e.getMessage(); }
    }
    static String extract(String json,String key){
        int i=json.indexOf("\""+key+"\""); if(i<0) return "";
        int c=json.indexOf(":",i); int q1=json.indexOf("\"",c+1); int q2=json.indexOf("\"",q1+1);
        return json.substring(q1+1,q2);
    }
    static int intVal(String json,String key,int def){
        int i=json.indexOf("\""+key+"\""); if(i<0) return def;
        int c=json.indexOf(":",i); int end=json.indexOf(",",c); if(end<0) end=json.indexOf("}",c);
        String v=json.substring(c+1,end).trim().replace("\"","");
        try{return Integer.parseInt(v);}catch(Exception e){return def;}
    }
}
