package com.abel.aisoftwareengineer.tools;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathGuard {
    public static Path resolve(String root, String rel) {
        Path r = Paths.get(root).toAbsolutePath().normalize();
        Path p = r.resolve(rel).normalize();
        if (!p.startsWith(r)) throw new SecurityException("Path traversal blocked");
        return p;
    }
}
