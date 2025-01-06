package com.cts.controller;

import com.cts.entity.Job;
import com.cts.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateJob() {
        Job job = new Job();
        job.setTitle("Test Job");
        when(jobService.createJob(job)).thenReturn(job);

        ResponseEntity<Job> response = jobController.createJob(job);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(job, response.getBody());
        verify(jobService, times(1)).createJob(job);
    }

    @Test
    void testGetAllJobs() {
        Job job1 = new Job();
        job1.setTitle("Job 1");
        Job job2 = new Job();
        job2.setTitle("Job 2");
        List<Job> jobs = Arrays.asList(job1, job2);
        when(jobService.getAllJobs()).thenReturn(jobs);

        ResponseEntity<List<Job>> response = jobController.getAllJobs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobs, response.getBody());
        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    void testGetJobById() {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Test Job");
        when(jobService.getJobById(1L)).thenReturn(Optional.of(job));

        ResponseEntity<Job> response = jobController.getJobById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(job, response.getBody());
        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobService.getJobById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.getJobById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    void testUpdateJob() {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Updated Job");
        when(jobService.getJobById(1L)).thenReturn(Optional.of(job));
        when(jobService.updateJob(job)).thenReturn(job);

        ResponseEntity<Job> response = jobController.updateJob(1L, job);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(job, response.getBody());
        verify(jobService, times(1)).updateJob(job);
    }

    @Test
    void testDeleteJob() {
        doNothing().when(jobService).deleteJob(1L);

        ResponseEntity<Void> response = jobController.deleteJob(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(jobService, times(1)).deleteJob(1L);
    }
}