package com.abel.aisoftwareengineer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aise")
public class AiseProperties {
    public Llm llm = new Llm();
    public Sandbox sandbox = new Sandbox();
    public Limits limits = new Limits();
    public Review review = new Review();
    public Indexing indexing = new Indexing();

    public static class Llm {
        public String defaultModel = "qwen3:8b";
        public String engineerModel = "qwen3:8b";
        public String reviewerModel = "qwen3:8b";
        public String embeddingModel = "nomic-embed-text";
        public int timeoutSeconds = 120;
        public int maxRetries = 2;
    }
    public static class Sandbox {
        public String allowedCommands = "mvn,gradle,java,npm,git,javac";
        public int cpuLimit = 1;
        public int memoryMb = 1024;
        public int pidsLimit = 256;
        public boolean networkEnabled = false;
        public boolean prepNetworkEnabled = true;
    }
    public static class Limits {
        public int maxIterations = 25;
        public int maxTokensPerTask = 200000;
        public int commandTimeoutSeconds = 120;
        public int taskTimeoutSeconds = 600;
        public int toolOutputMaxChars = 8000;
        public int loopDetectionRepeat = 3;
    }
    public static class Review {
        public int maxRounds = 2;
    }
    public static class Indexing {
        public boolean enabled = false;
    }
}
