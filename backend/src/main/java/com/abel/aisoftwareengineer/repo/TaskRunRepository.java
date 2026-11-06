package com.abel.aisoftwareengineer.repo;

import com.abel.aisoftwareengineer.domain.TaskRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRunRepository extends JpaRepository<TaskRun, Long> { }
