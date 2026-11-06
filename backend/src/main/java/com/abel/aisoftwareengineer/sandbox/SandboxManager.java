package com.abel.aisoftwareengineer.sandbox;

import com.abel.aisoftwareengineer.config.AiseProperties;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class SandboxManager {
    private final AiseProperties props;
    private final DockerClient docker;

    public SandboxManager(AiseProperties props) {
        this.props = props;
        var config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        var http = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        this.docker = DockerClientImpl.getInstance(config, http);
    }

    public String createContainer(String workDir, String image) {
        HostConfig hc = HostConfig.newHostConfig()
                .withMemory((long) props.sandbox.memoryMb * 1024 * 1024)
                .withCpuCount((long) props.sandbox.cpuLimit)
                .withPidsLimit((long) props.sandbox.pidsLimit)
                .withNetworkMode(props.sandbox.networkEnabled ? "bridge" : "none")
                .withBinds(com.github.dockerjava.api.model.Bind.parse(workDir + ":/work:rw"));
        CreateContainerResponse c = docker.createContainerCmd(image)
                .withCmd("tail", "-f", "/dev/null")
                .withWorkingDir("/work")
                .withUser("1000:1000")
                .withHostConfig(hc)
                .exec();
        docker.startContainerCmd(c.getId()).exec();
        return c.getId();
    }

    public String exec(String containerId, String command, int timeoutSec) {
        try {
            var exec = docker.execCreateCmd(containerId)
                    .withCmd("sh", "-c", command)
                    .withAttachStdout(true).withAttachStderr(true).exec();
            var stream = docker.execStartCmd(exec.getId());
            StringBuilder out = new StringBuilder();
            stream.exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<com.github.dockerjava.api.model.Frame>() {
                public void onNext(com.github.dockerjava.api.model.Frame frame) {
                    out.append(new String(frame.getPayload()));
                }
            }).awaitCompletion();
            String result = out.toString();
            if (result.length() > props.limits.toolOutputMaxChars) {
                result = result.substring(0, props.limits.toolOutputMaxChars) + "\n...[truncated]";
            }
            return result;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    public void remove(String containerId) {
        try { docker.removeContainerCmd(containerId).withForce(true).exec(); } catch (Exception ignored) {}
    }
}
