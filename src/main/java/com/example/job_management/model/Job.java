package com.example.job_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.job_management.Common.JobState;
import com.example.job_management.dto.JobDto;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING) // Persist enum as a String in the database
    private JobState state;

    private int priority;

    @Column(nullable = true)
    private LocalDateTime scheduledTime;

    public Job() {
    }

    public Job(JobDto jobDto) {
        this.type = jobDto.getType();
        this.priority = jobDto.getPriority();
        this.scheduledTime = jobDto.getScheduledTime();
    }

    public Job(String type, JobState state, int priority, LocalDateTime scheduledTime) {
        this.type = type;
        this.state = state;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
