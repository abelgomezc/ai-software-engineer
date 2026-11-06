package com.abel.aisoftwareengineer.repo;

import com.abel.aisoftwareengineer.domain.RunStep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RunStepRepository extends JpaRepository<RunStep, Long> {
    List<RunStep> findByTaskRunIdOrderByStepNumberAsc(Long taskRunId);
}
