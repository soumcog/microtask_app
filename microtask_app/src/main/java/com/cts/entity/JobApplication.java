package com.cts.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // The job being applied for

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker; // The user applying for the job

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status; // Status of the application

    @CreationTimestamp
    private LocalDateTime appliedAt; // Timestamp when the application was submitted

    public enum ApplicationStatus {
        PENDING, // Default status when an application is submitted
        APPROVED, // Employer approves the application
        REJECTED // Employer rejects the application
    }
}