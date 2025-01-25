package com.cts.service;

import com.cts.entity.JobApplication;
import com.cts.entity.User;
import com.cts.exception.ResourceNotFoundException;
import com.cts.exception.UnauthorizedAccessException;
import com.cts.repository.JobApplicationRepository;
import com.cts.repository.JobRepository;
import com.cts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // Apply for a job
    public JobApplication applyForJob(Long jobId, Long workerId) {
        var job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));
        var worker = userRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + workerId));

        // Check if the user is a worker
        if (!worker.getRole().equals(User.Role.WORKER)) {
            throw new UnauthorizedAccessException("Only workers can apply for jobs.");
        }

        // Check if the worker has already applied for this job
        if (jobApplicationRepository.existsByJobIdAndWorkerId(jobId, workerId)) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        // Create and save the job application
        var jobApplication = JobApplication.builder()
                .job(job)
                .worker(worker)
                .status(JobApplication.ApplicationStatus.PENDING)
                .build();

        return jobApplicationRepository.save(jobApplication);
    }

    // Get all applications for a job (for employers)
    public List<JobApplication> getApplicationsForJob(Long jobId, Long employerId) {
        var job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));

        // Ensure the employer owns the job
        if (!job.getCreatedBy().equals(employerId)) {
            throw new UnauthorizedAccessException("You are not authorized to view applications for this job.");
        }

        return jobApplicationRepository.findByJobId(jobId);
    }

    // Update application status (approve/reject)
    public JobApplication updateApplicationStatus(Long applicationId, Long employerId, JobApplication.ApplicationStatus status) {
        var application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + applicationId));

        // Ensure the employer owns the job
        if (!application.getJob().getCreatedBy().equals(employerId)) {
            throw new UnauthorizedAccessException("You are not authorized to update this application.");
        }

        application.setStatus(status);
        return jobApplicationRepository.save(application);
    }

    // Get all applications by a worker
    public List<JobApplication> getApplicationsByWorker(Long workerId) {
        return jobApplicationRepository.findByWorkerId(workerId);
    }
}