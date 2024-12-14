package com.Project.AppApplication.controller;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Job;
import com.Project.AppApplication.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/companies/{companyId}")
    public List<Job> getJobsByCompany(@PathVariable Long companyId) {
        return jobService.getJobsByCompanyId(companyId);
    }

    @PostMapping("/{jobId}/register/{candidatoId}")
    public ResponseEntity<Job> registerCandidateToJob(
            @PathVariable Long jobId,
            @PathVariable Long candidatoId) {
        try {
            Job updatedJob = jobService.registerCandidateToJob(jobId, candidatoId);
            return ResponseEntity.ok(updatedJob);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}