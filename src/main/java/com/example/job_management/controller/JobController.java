package com.example.job_management.controller;

import com.example.job_management.Common.JobState;
import com.example.job_management.model.Job;
import com.example.job_management.service.JobService;
import com.example.job_management.dto.JobDto;
import com.example.job_management.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Create a new job", description = "Adds a new job to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Job.class), examples = @ExampleObject(name = "Example Response", value = "{\"id\": 1, \"name\": \"Example Job\", \"state\": \"QUEUED\"}", summary = "An example job response"))),
            @ApiResponse(responseCode = "400", description = "Error Message", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job email :  Scheduled time cannot be in the past\", \"status\": 400}", summary = "An example error response")))
    })
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobDto job) {
        String error = jobService.validateJob(job);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(error, HttpStatus.BAD_REQUEST.value()));

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(job));

    }

    @Operation(summary = "Create a bulk of new jobs", description = "Adds a list off new job to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jobs created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Job.class), examples = @ExampleObject(name = "Example Response", value = "[{\"id\": 1, \"name\": \"Example Job\", \"state\": \"QUEUED\"}]", summary = "An example job list response"))),
            @ApiResponse(responseCode = "400", description = "Error Message", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Scheduled time cannot be in the past\", \"status\": 400}", summary = "An example error response")))
    })
    @PostMapping("/bulk")
    public ResponseEntity<?> createJobs(@RequestBody List<JobDto> jobs) {
        String error = jobService.validateJobs(jobs);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(error, HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJobs(jobs));
    }

    @Operation(summary = "Get all jobs", description = "Returns a list of all jobs in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of jobs returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Job.class), examples = @ExampleObject(name = "Example Response", value = "[{\"id\": 1, \"name\": \"Example Job\", \"state\": \"QUEUED\"}]", summary = "An example job list response")))
    })
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @Operation(summary = "Get a job by ID", description = "Returns a job by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Job.class), examples = @ExampleObject(name = "Example Response", value = "{\"id\": 1, \"name\": \"Example Job\", \"state\": \"QUEUED\"}", summary = "An example job response"))),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job not found\", \"status\": 404}", summary = "An example error response")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Job not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(job);
    }

    @Operation(summary = "Get the status of a job", description = "Returns the status of a job by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job status returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"QUEUED\", \"status\": 200}", summary = "An example job status response"))),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job not found\", \"status\": 404}", summary = "An example error response")))
    })
    @GetMapping("/status/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Job not found", HttpStatus.NOT_FOUND.value()));
        }
        return ResponseEntity.ok(new ResponseDto(job.getState().toString(), HttpStatus.OK.value()));
    }

    @Operation(summary = "Delete a job", description = "Deletes a job by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job deleted successfully\", \"status\": 200}", summary = "An example success response"))),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job not found\", \"status\": 404}", summary = "An example error response"))),
            @ApiResponse(responseCode = "409", description = "Cannot delete a running job", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Cannot delete a running job\", \"status\": 409}", summary = "An example error response")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {

        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Job not found", HttpStatus.NOT_FOUND.value()));
        }

        if (job.getState() == JobState.RUNNING) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDto("Cannot delete a running job", HttpStatus.CONFLICT.value()));
        }

        jobService.deleteJob(id);

        return ResponseEntity.ok(new ResponseDto("Job deleted successfully", HttpStatus.OK.value()));
    }

    @Operation(summary = "Retry a failed job", description = "Retries a job that is in a failed state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job retried successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job retried successfully\", \"status\": 200}", summary = "An example success response"))),
            @ApiResponse(responseCode = "404", description = "Job not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job not found\", \"status\": 404}", summary = "An example error response"))),
            @ApiResponse(responseCode = "409", description = "Job is not in failed state", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(name = "Example Response", value = "{\"message\": \"Job is not in failed state\", \"status\": 409}", summary = "An example error response")))
    })
    @PatchMapping("/retry/{id}")
    public ResponseEntity<?> retryJob(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("Job not found", HttpStatus.NOT_FOUND.value()));
        }

        if (job.getState() != JobState.FAILED) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDto("Job is not in failed state", HttpStatus.CONFLICT.value()));
        }

        // job = jobService.createJob(new JobDto(job));
        jobService.retryJob(id);

        return ResponseEntity
                .ok(new ResponseDto("Job retried successfully with new state : " + job.getState(),
                        HttpStatus.OK.value()));
    }
}
