package com.example.job_management.dto;

import java.time.LocalDateTime;

import com.example.job_management.model.Job;

public class JobDto {
    private String type;
    private LocalDateTime scheduledTime;
    private int priority;

    public JobDto() {
    }

    public JobDto(String type, LocalDateTime scheduledTime, int priority) {
        this.type = type;
        this.scheduledTime = scheduledTime;
        this.priority = priority;
    }

    public JobDto(Job job) {
        this.type = job.getType();
        this.scheduledTime = job.getScheduledTime();
        this.priority = job.getPriority();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
