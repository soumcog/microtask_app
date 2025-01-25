package com.cts.controller;

import com.cts.entity.JobApplication;
import com.cts.repository.UserRepository;
import com.cts.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository; // Inject UserRepository

    // Apply for a job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplication> applyForJob(@PathVariable Long jobId) {
        var workerId = getCurrentUserId(); // Get the ID of the logged-in worker
        var application = jobApplicationService.applyForJob(jobId, workerId);
        return ResponseEntity.ok(application);
    }

    // Get applications for a job (for employers)
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplication>> getApplicationsForJob(@PathVariable Long jobId) {
        var employerId = getCurrentUserId(); // Get the ID of the logged-in employer
        var applications = jobApplicationService.getApplicationsForJob(jobId, employerId);
        return ResponseEntity.ok(applications);
    }

    // Update application status (approve/reject)
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<JobApplication> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam JobApplication.ApplicationStatus status
    ) {
        var employerId = getCurrentUserId(); // Get the ID of the logged-in employer
        var application = jobApplicationService.updateApplicationStatus(applicationId, employerId, status);
        return ResponseEntity.ok(application);
    }

    // Get all applications by a worker
    @GetMapping("/worker")
    public ResponseEntity<List<JobApplication>> getApplicationsByWorker() {
        var workerId = getCurrentUserId(); // Get the ID of the logged-in worker
        var applications = jobApplicationService.getApplicationsByWorker(workerId);
        return ResponseEntity.ok(applications);
    }

    // Helper method to get the current user's ID
    private Long getCurrentUserId() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
