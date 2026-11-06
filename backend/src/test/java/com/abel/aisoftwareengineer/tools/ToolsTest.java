package com.abel.aisoftwareengineer.tools;

import com.abel.aisoftwareengineer.config.AiseProperties;
import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {
    Path root; AiseProperties props;
    @BeforeEach void setup() throws Exception {
        root = Files.createTempDirectory("t");
        props = new AiseProperties();
        Files.writeString(root.resolve("a.txt"), "hello world\nfoo bar\n");
    }
    @Test void readFile() { assertEquals(true, new ReadFileTool(root.toString(),props).execute("{\"path\":\"a.txt\"}").contains("hello")); }
    @Test void listFiles() { assertTrue(new ListFilesTool(root.toString(),props).execute("{}").contains("a.txt")); }
    @Test void searchCode() { assertTrue(new SearchCodeTool(root.toString(),props).execute("{\"query\":\"foo\"}").contains("a.txt")); }
    @Test void editMissing() { assertTrue(new EditFileTool(root.toString(),props).execute("{\"path\":\"a.txt\",\"old_str\":\"nope\",\"new_str\":\"x\"}").startsWith("ERROR")); }
    @Test void editNonUnique() throws Exception {
        Files.writeString(root.resolve("b.txt"), "x\nx\n");
        assertTrue(new EditFileTool(root.toString(),props).execute("{\"path\":\"b.txt\",\"old_str\":\"x\",\"new_str\":\"y\"}").startsWith("ERROR"));
    }
    @Test void createExisting() { assertTrue(new CreateFileTool(root.toString(),props).execute("{\"path\":\"a.txt\",\"content\":\"z\"}").startsWith("ERROR")); }
    @Test void createNew() { assertEquals(true, new CreateFileTool(root.toString(),props).execute("{\"path\":\"c.txt\",\"content\":\"z\"}").startsWith("OK")); }
    @Test void finish() { assertTrue(new FinishTool(props).execute("{\"summary\":\"done\"}").contains("FINISHED")); }
}
