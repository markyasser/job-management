package com.example.job_management;

import com.example.job_management.Common.JobState;
import com.example.job_management.model.Job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class JobTest {

    private Job job;

    @BeforeEach
    void setUp() {
        // Create a new Job object before each test
        job = new Job("Test Type", JobState.QUEUED, 1, LocalDateTime.now());
    }

    @Test
    void testJobConstructor() {

        LocalDateTime scheduledTime = LocalDateTime.of(2025, 1, 27, 10, 30, 0, 0);

        Job newJob = new Job("Test Type", JobState.RUNNING, 2, scheduledTime);

        assertNotNull(newJob);
        assertEquals("Test Type", newJob.getType());
        assertEquals(JobState.RUNNING, newJob.getState());
        assertEquals(2, newJob.getPriority());
        assertEquals(scheduledTime, newJob.getScheduledTime());
    }

    @Test
    void testGetSetId() {

        job.setId(1L);

        assertEquals(1L, job.getId());
    }

    @Test
    void testGetSetType() {

        job.setType("Job Type");

        assertEquals("Job Type", job.getType());
    }

    @Test
    void testGetSetState() {

        job.setState(JobState.RUNNING);

        assertEquals(JobState.RUNNING, job.getState());
    }

    @Test
    void testGetSetPriority() {

        job.setPriority(5);

        assertEquals(5, job.getPriority());
    }

    @Test
    void testGetSetScheduledTime() {

        LocalDateTime scheduledTime = LocalDateTime.of(2025, 1, 27, 10, 30, 0, 0);

        job.setScheduledTime(scheduledTime);

        assertEquals(scheduledTime, job.getScheduledTime());
    }

    @Test
    void testJobDefaultConstructor() {

        Job defaultJob = new Job();

        assertNull(defaultJob.getId()); // ID should be null for a newly created job
        assertNull(defaultJob.getType());
        assertNull(defaultJob.getState());
        assertEquals(0, defaultJob.getPriority()); // Default priority is 0
        assertNull(defaultJob.getScheduledTime());
    }
}
