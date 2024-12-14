package com.Project.AppApplication.services;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Job;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.company.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByCompanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    public Job registerCandidateToJob(Long jobId, Long candidatoId) {
        // Busca o job pelo ID
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            throw new IllegalArgumentException("Job com ID " + jobId + " não encontrado.");
        }

        // Busca o candidato pelo ID
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(candidatoId);
        if (candidatoOptional.isEmpty()) {
            throw new IllegalArgumentException("Candidato com ID " + candidatoId + " não encontrado.");
        }

        Job job = jobOptional.get();
        Candidato candidato = candidatoOptional.get();

        // Evita duplicidade (candidato já registrado)
        if (!job.getCandidates().contains(candidato)) {
            // Adiciona o candidato ao job
            job.getCandidates().add(candidato);

            // Salva o job atualizado
            jobRepository.save(job);
        }

        // Retorna o job atualizado
        return job;
    }

}