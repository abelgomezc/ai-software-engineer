package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.abel.aisoftwareengineer.repo.RepoManager;
import java.nio.file.Path;

public class GitDiffTool implements Tool {
    private final String root; private final AiseProperties props; private final RepoManager repo;
    public GitDiffTool(String root, AiseProperties props, RepoManager repo){this.root=root;this.props=props;this.repo=repo;}
    public String name(){return "git_diff";}
    public String description(){return "Show current diff";}
    public String execute(String inputJson){ return repo.diff(Path.of(root)); }
}
