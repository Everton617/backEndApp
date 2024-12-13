package com.Project.AppApplication.controller;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Company;
import com.Project.AppApplication.domain.empresa.Job;
import com.Project.AppApplication.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class EmpresaController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<Company> listarTodos() {
        return companyService.listarCompany();
    }

    @PostMapping("/{companyId}/jobs")
    public ResponseEntity<Job> createJob(@PathVariable Long companyId, @RequestBody Job job) {
        try {
            Job createdJob = companyService.createJob(companyId, job);
            return ResponseEntity.ok(createdJob);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        try {
            Company createdCompany = companyService.createCompany(company);
            return ResponseEntity.ok(createdCompany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
