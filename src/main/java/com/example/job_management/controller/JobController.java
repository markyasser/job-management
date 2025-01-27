package com.example.job_management.controller;

import com.example.job_management.Common.JobState;
import com.example.job_management.model.Job;
import com.example.job_management.service.JobService;
import com.example.job_management.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(job));

    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("Job not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(job);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("Job not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(job.getState());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {

        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("Job not found", HttpStatus.NOT_FOUND.value()));
        }

        if (job.getState() == JobState.RUNNING) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Response("Cannot delete a running job", HttpStatus.CONFLICT.value()));
        }

        jobService.deleteJob(id);

        return ResponseEntity.ok(new Response("Job deleted successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/retry/{id}")
    public ResponseEntity<?> retryJob(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response("Job not found", HttpStatus.NOT_FOUND.value()));
        }

        if (job.getState() != JobState.FAILED) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Response("Job is not in failed state", HttpStatus.CONFLICT.value()));
        }

        job.setState(JobState.QUEUED);
        jobService.createJob(job);

        return ResponseEntity.ok(new Response("Job retried successfully", HttpStatus.OK.value()));
    }
}
