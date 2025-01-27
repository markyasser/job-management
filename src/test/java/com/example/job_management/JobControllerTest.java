package com.example.job_management;

import com.example.job_management.model.Job;
import com.example.job_management.service.JobService;
import com.example.job_management.Common.JobState;
import com.example.job_management.controller.JobController;
import com.example.job_management.dto.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @Test
    public void createJob_Success() {
        Job job = new Job();
        job.setId(1L);
        job.setType("Test Job");

        // Mock the behavior of jobService
        when(jobService.createJob(job)).thenReturn(job);

        ResponseEntity<Job> response = jobController.createJob(job);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Job", response.getBody().getType());
        verify(jobService, times(1)).createJob(job);
    }

    @Test
    public void getJobById_JobNotFound() {
        Long jobId = 1L;

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(null);

        ResponseEntity<?> response = jobController.getJobById(jobId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Response);
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void getJobStatus_Success() {
        Long jobId = 1L;
        Job job = new Job();
        job.setState(JobState.QUEUED);

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(job);

        ResponseEntity<?> response = jobController.getJobStatus(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JobState.QUEUED, response.getBody());
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void deleteJob_JobNotFound() {
        Long jobId = 1L;

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(null);

        ResponseEntity<?> response = jobController.deleteJob(jobId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Response);
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void deleteJob_CannotDeleteRunningJob() {
        Long jobId = 1L;
        Job job = new Job();
        job.setState(JobState.RUNNING);

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(job);

        ResponseEntity<?> response = jobController.deleteJob(jobId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Response);
        verify(jobService, times(1)).getJobById(jobId);
    }
}
