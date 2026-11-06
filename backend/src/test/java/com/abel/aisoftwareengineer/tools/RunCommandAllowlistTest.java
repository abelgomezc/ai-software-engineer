package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.abel.aisoftwareengineer.sandbox.SandboxManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RunCommandAllowlistTest {
    @Test void rejectsNotAllowed() {
        // just check logic via AiseProperties
        AiseProperties p = new AiseProperties();
        assertFalse(java.util.Arrays.asList(p.sandbox.allowedCommands.split(",")).contains("curl"));
    }
}
