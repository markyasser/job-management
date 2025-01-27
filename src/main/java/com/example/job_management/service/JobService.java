package com.example.job_management.service;

import com.example.job_management.Common.JobState;
import com.example.job_management.model.Job;
import com.example.job_management.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {
        job.setState(JobState.QUEUED);
        return jobRepository.save(job);
    }

    public List<Job> createJobs(List<Job> jobs) {
        for (Job job : jobs) {
            job.setState(JobState.QUEUED);
        }
        return jobRepository.saveAll(jobs);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public Boolean isJobRunning(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return job != null && job.getState() == JobState.RUNNING;
    }
}
