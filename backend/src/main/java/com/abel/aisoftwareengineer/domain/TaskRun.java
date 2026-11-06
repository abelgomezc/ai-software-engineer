package com.abel.aisoftwareengineer.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task_run")
public class TaskRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "repo_source", nullable = false) private String repoSource;
    @Column(name = "task_description", nullable = false, columnDefinition = "TEXT") private String taskDescription;
    @Column(nullable = false) private String status = "PENDING";
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();
    @Column(name = "finished_at") private Instant finishedAt;
    @Column private int iterations = 0;
    @Column(name = "tokens_in") private int tokensIn = 0;
    @Column(name = "tokens_out") private int tokensOut = 0;
    @Column(name = "final_diff", columnDefinition = "TEXT") private String finalDiff;
    @Column(columnDefinition = "TEXT") private String summary;
    @Column(name = "branch_name") private String branchName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRepoSource() { return repoSource; }
    public void setRepoSource(String v) { this.repoSource = v; }
    public String getTaskDescription() { return taskDescription; }
    public void setTaskDescription(String v) { this.taskDescription = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant v) { this.createdAt = v; }
    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant v) { this.finishedAt = v; }
    public int getIterations() { return iterations; }
    public void setIterations(int v) { this.iterations = v; }
    public int getTokensIn() { return tokensIn; }
    public void setTokensIn(int v) { this.tokensIn = v; }
    public int getTokensOut() { return tokensOut; }
    public void setTokensOut(int v) { this.tokensOut = v; }
    public String getFinalDiff() { return finalDiff; }
    public void setFinalDiff(String v) { this.finalDiff = v; }
    public String getSummary() { return summary; }
    public void setSummary(String v) { this.summary = v; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String v) { this.branchName = v; }
}
