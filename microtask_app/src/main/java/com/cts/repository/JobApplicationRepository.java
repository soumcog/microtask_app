package com.cts.repository;

import com.cts.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Find applications for a specific job
    List<JobApplication> findByJobId(Long jobId);

    // Find applications by a specific worker
    List<JobApplication> findByWorkerId(Long workerId);

    // Custom query to check if a worker has already applied for a job
    @Query("SELECT CASE WHEN COUNT(ja) > 0 THEN true ELSE false END FROM JobApplication ja WHERE ja.job.id = :jobId AND ja.worker.id = :workerId")
    boolean existsByJobIdAndWorkerId(@Param("jobId") Long jobId, @Param("workerId") Long workerId);
}