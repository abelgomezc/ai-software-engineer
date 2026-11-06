package com.abel.aisoftwareengineer.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PathGuardTest {
    @Test
    void blocksTraversal() {
        assertThrows(SecurityException.class, () -> PathGuard.resolve("C:\\repo", "..\\..\\Windows\\system32"));
    }
    @Test
    void allowsInside() {
        assertNotNull(PathGuard.resolve("C:\\repo", "src\\Main.java"));
    }
}
