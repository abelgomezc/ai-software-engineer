package com.abel.aisoftwareengineer.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "run_step")
public class RunStep {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_run_id", nullable = false) private Long taskRunId;
    @Column(name = "step_number", nullable = false) private int stepNumber;
    private String role;
    @Column(name = "thought_summary", columnDefinition = "TEXT") private String thoughtSummary;
    @Column(name = "tool_name") private String toolName;
    @Column(name = "tool_input", columnDefinition = "jsonb") private String toolInput;
    @Column(name = "tool_output", columnDefinition = "TEXT") private String toolOutput;
    @Column(name = "duration_ms") private Long durationMs;
    private String status;
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskRunId() { return taskRunId; }
    public void setTaskRunId(Long v) { this.taskRunId = v; }
    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int v) { this.stepNumber = v; }
    public String getRole() { return role; }
    public void setRole(String v) { this.role = v; }
    public String getThoughtSummary() { return thoughtSummary; }
    public void setThoughtSummary(String v) { this.thoughtSummary = v; }
    public String getToolName() { return toolName; }
    public void setToolName(String v) { this.toolName = v; }
    public String getToolInput() { return toolInput; }
    public void setToolInput(String v) { this.toolInput = v; }
    public String getToolOutput() { return toolOutput; }
    public void setToolOutput(String v) { this.toolOutput = v; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long v) { this.durationMs = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant v) { this.createdAt = v; }
}
