package com.example.job_management;

import com.example.job_management.model.Job;
import com.example.job_management.service.JobService;
import com.example.job_management.Common.JobState;
import com.example.job_management.controller.JobController;
import com.example.job_management.dto.JobDto;
import com.example.job_management.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;

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
        JobDto jobDto = new JobDto(job);

        // Mock the behavior of jobService
        when(jobService.createJob(jobDto)).thenReturn(job);

        @SuppressWarnings("unchecked")
        ResponseEntity<Job> response = (ResponseEntity<Job>) jobController.createJob(jobDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Job", response.getBody().getType());
        verify(jobService, times(1)).createJob(jobDto);
    }

    @Test
    public void createJob_InvalidJobType() {
        Job job = new Job();
        job.setId(1L);
        job.setType("");
        JobDto jobDto = new JobDto(job);

        when(jobService.validateJob(jobDto)).thenReturn("Invalid job type");
        ResponseEntity<?> response = jobController.createJob(jobDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(0)).createJob(jobDto);
    }

    @Test
    public void createBulkJobs_Success() {
        Job job1 = new Job();
        job1.setId(1L);
        job1.setType("Test Job 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setType("Test Job 2");

        JobDto jobDto1 = new JobDto(job1);
        JobDto jobDto2 = new JobDto(job2);

        when(jobService.createJobs(anyList())).thenReturn(List.of(job1, job2));

        @SuppressWarnings("unchecked")
        ResponseEntity<List<Job>> response = (ResponseEntity<List<Job>>) jobController
                .createJobs(List.of(jobDto1, jobDto2));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Job 1", response.getBody().get(0).getType());
        assertEquals("Test Job 2", response.getBody().get(1).getType());
        verify(jobService, times(1)).createJobs(List.of(jobDto1, jobDto2));
    }

    @Test
    public void createBulkJobs_InvalidJobType() {
        Job job1 = new Job();
        job1.setId(1L);
        job1.setType("Test Job 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setType("");

        JobDto jobDto1 = new JobDto(job1);
        JobDto jobDto2 = new JobDto(job2);

        when(jobService.validateJobs(List.of(jobDto1, jobDto2))).thenReturn("Job Test Job 2 : Invalid job type");
        ResponseEntity<?> response = jobController.createJobs(List.of(jobDto1, jobDto2));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(0)).createJobs(List.of(jobDto1, jobDto2));
    }

    @Test
    public void getAllJobs_Success() {
        Job job1 = new Job();
        job1.setId(1L);
        job1.setType("Test Job 1");

        Job job2 = new Job();
        job2.setId(2L);
        job2.setType("Test Job 2");

        // Mock the behavior of jobService
        when(jobService.getAllJobs()).thenReturn(List.of(job1, job2));

        ResponseEntity<List<Job>> response = jobController.getAllJobs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Job 1", response.getBody().get(0).getType());
        assertEquals("Test Job 2", response.getBody().get(1).getType());
        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    public void getById_Success() {
        Long jobId = 1L;
        Job job = new Job();
        job.setId(jobId);
        job.setType("Test Job");

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(job);

        ResponseEntity<?> response = jobController.getJobById(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Job", ((Job) response.getBody()).getType());
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void getJobById_JobNotFound() {
        Long jobId = 1L;

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(null);

        ResponseEntity<?> response = jobController.getJobById(jobId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
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
        assertTrue(response.getBody() instanceof ResponseDto);
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
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void retryJob_JobNotFound() {
        Long jobId = 1L;

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(null);

        ResponseEntity<?> response = jobController.retryJob(jobId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void retryJob_JobNotInFailedState() {
        Long jobId = 1L;
        Job job = new Job();
        job.setState(JobState.QUEUED);

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(job);

        ResponseEntity<?> response = jobController.retryJob(jobId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(1)).getJobById(jobId);
    }

    @Test
    public void retryJob_Success() {
        Long jobId = 1L;
        Job job = new Job();
        job.setState(JobState.FAILED);

        // Mock the behavior of jobService
        when(jobService.getJobById(jobId)).thenReturn(job);

        ResponseEntity<?> response = jobController.retryJob(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDto);
        verify(jobService, times(1)).getJobById(jobId);
        verify(jobService, times(1)).createJob(new JobDto(job));
    }
}
