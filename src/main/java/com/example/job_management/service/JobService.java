package com.example.job_management.service;

import com.example.job_management.Common.JobState;
import com.example.job_management.dto.JobDto;
import com.example.job_management.model.Job;
import com.example.job_management.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public String validateJob(JobDto job) {
        if (job.getType() == null || job.getType().length() == 0) {
            return "Invalid job type";
        }
        if (job.getPriority() < 0) {
            return "Invalid job priority";
        }
        if (job.getScheduledTime() != null && job.getScheduledTime().isBefore(java.time.LocalDateTime.now())) {
            return "Scheduled time cannot be in the past";
        }
        return null;
    }

    public String validateJobs(List<JobDto> jobs) {
        for (JobDto job : jobs) {
            String error = validateJob(job);
            if (error != null) {
                return "Job " + job.getType() + " : " + error;
            }
        }
        return null;
    }

    public void retryJob(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job != null) {
            job.setState(JobState.QUEUED);
            jobRepository.save(job);
        }
    }

    public Job createJob(JobDto jobdto) {
        Job job = new Job(jobdto);
        if (job.getScheduledTime() == null) {
            int random = (int) (Math.random() * 4);
            job.setState(JobState.values()[random]);
        } else {
            job.setState(JobState.QUEUED);
        }
        return jobRepository.save(job);
    }

    public List<Job> createJobs(List<JobDto> jobs) {
        List<Job> jobList = new ArrayList<>();
        for (JobDto jobdto : jobs) {
            Job job = new Job(jobdto);
            if (job.getScheduledTime() == null) {
                int random = (int) (Math.random() * 4);
                job.setState(JobState.values()[random]);
            } else {
                job.setState(JobState.QUEUED);
            }
            jobList.add(job);
        }
        return jobRepository.saveAll(jobList);
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
