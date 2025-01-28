package com.example.job_management;

import com.example.job_management.Common.JobState;
import com.example.job_management.dto.JobDto;
import com.example.job_management.model.Job;
import com.example.job_management.repository.JobRepository;
import com.example.job_management.service.JobService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job job1;
    private Job job2;

    @BeforeEach
    void setUp() {
        // Create mock jobs before each test
        job1 = new Job();
        job1.setId(1L);
        job1.setState(JobState.QUEUED);

        job2 = new Job();
        job2.setId(2L);
        job2.setState(JobState.QUEUED);
    }

    @Test
    void testCreateJob() {

        when(jobRepository.save(any(Job.class))).thenReturn(job1);

        Job createdJob = jobService.createJob(new JobDto(job1));

        assertNotNull(createdJob);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testCreateJobs() {

        List<Job> jobs = Arrays.asList(job1, job2);

        when(jobRepository.saveAll(any(List.class))).thenReturn(jobs);

        List<Job> createdJobs = jobService.createJobs(Arrays.asList(new JobDto(job1), new JobDto(job2)));

        assertEquals(2, createdJobs.size());
        verify(jobRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    void testGetAllJobs() {

        List<Job> jobs = Arrays.asList(job1, job2);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> allJobs = jobService.getAllJobs();

        assertNotNull(allJobs);
        assertEquals(2, allJobs.size());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testGetJobById_found() {

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        Job retrievedJob = jobService.getJobById(1L);

        assertNotNull(retrievedJob);
        assertEquals(1L, retrievedJob.getId());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testGetJobById_notFound() {

        when(jobRepository.findById(3L)).thenReturn(Optional.empty());

        Job retrievedJob = jobService.getJobById(3L);

        assertNull(retrievedJob);
        verify(jobRepository, times(1)).findById(3L);
    }

    @Test
    void testDeleteJob() {

        jobService.deleteJob(1L);

        verify(jobRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIsJobRunning_true() {

        job1.setState(JobState.RUNNING);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        Boolean isRunning = jobService.isJobRunning(1L);

        assertTrue(isRunning);
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testIsJobRunning_false() {

        job1.setState(JobState.QUEUED);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        Boolean isRunning = jobService.isJobRunning(1L);

        assertFalse(isRunning);
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testIsJobRunning_jobNotFound() {

        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        Boolean isRunning = jobService.isJobRunning(1L);

        assertFalse(isRunning);
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testRetryJob_jobNotFound() {

        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        jobService.retryJob(1L);

        verify(jobRepository, times(0)).save(any(Job.class));
    }

    @Test
    void testRetryJob() {

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));

        jobService.retryJob(1L);

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testValidateJob_valid() {

        JobDto jobDto = new JobDto();
        jobDto.setType("type");
        jobDto.setScheduledTime(LocalDateTime.now());

        String error = jobService.validateJob(jobDto);

        assertNull(error);
    }

    @Test
    void testValidateJob_invalidType() {

        JobDto jobDto = new JobDto();
        jobDto.setType("");
        jobDto.setScheduledTime(LocalDateTime.now());

        String error = jobService.validateJob(jobDto);

        assertEquals("Invalid job type", error);
    }

    @Test
    void testValidateJob_invalidPriority() {

        JobDto jobDto = new JobDto();
        jobDto.setType("type");
        jobDto.setPriority(-1);
        jobDto.setScheduledTime(LocalDateTime.now());

        String error = jobService.validateJob(jobDto);

        assertEquals("Invalid job priority", error);
    }

    @Test
    void testValidateJob_invalidScheduledTime() {

        JobDto jobDto = new JobDto();
        jobDto.setType("type");
        jobDto.setScheduledTime(LocalDateTime.now().minusDays(1));

        String error = jobService.validateJob(jobDto);

        assertEquals("Scheduled time cannot be in the past", error);
    }

    @Test
    void testValidateJobs_valid() {

        JobDto jobDto1 = new JobDto();
        jobDto1.setType("type1");
        jobDto1.setScheduledTime(LocalDateTime.now());

        JobDto jobDto2 = new JobDto();
        jobDto2.setType("type2");
        jobDto2.setScheduledTime(LocalDateTime.now());

        String error = jobService.validateJobs(Arrays.asList(jobDto1, jobDto2));

        assertNull(error);
    }

}
