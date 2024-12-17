package com.cts.service;

import com.cts.entity.Job;
import com.cts.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job createJob(Job job);
    List<Job> getAllJobs();
    Optional<Job> getJobById(Long id);
    Job updateJob(Job job);
    void deleteJob(Long id);
}