package com.cts.service;

import com.cts.entity.Job;
import java.util.List;
import java.util.Optional;

public interface JobService {
    Job createJob(Job job);
    List<Job> getAllJobs();
    Optional<Job> getJobById(Long id);
    Job updateJob(Job job);
    void deleteJob(Long id);
    List<Job> getJobsByEmployer(Long employerId); // Add this method
}