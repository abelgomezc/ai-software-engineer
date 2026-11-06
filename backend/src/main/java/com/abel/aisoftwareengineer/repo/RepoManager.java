package com.abel.aisoftwareengineer.repo;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class RepoManager {
    public Path prepareWorkCopy(String repoSource, Path workRoot) throws Exception {
        Files.createDirectories(workRoot);
        Path target = workRoot.resolve("repo");
        if (repoSource.startsWith("http")) {
            Git.cloneRepository().setURI(repoSource).setDirectory(target.toFile()).call().close();
        } else {
            copyDir(new File(repoSource), target.toFile());
        }
        // init git if not
        if (!new File(target.toFile(), ".git").exists()) {
            Git.init().setDirectory(target.toFile()).call().close();
        }
        return target;
    }

    private void copyDir(File src, File dst) throws Exception {
        if (src.isDirectory()) {
            dst.mkdirs();
            for (File c : src.listFiles()) copyDir(c, new File(dst, c.getName()));
        } else {
            Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public String diff(Path workDir) {
        try (Git git = Git.open(workDir.toFile())) {
            return git.diff().call().toString();
        } catch (Exception e) { return "ERROR: " + e.getMessage(); }
    }
}
