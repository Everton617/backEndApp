package com.Project.AppApplication.repository.company;

import com.Project.AppApplication.domain.candidato.Experiencia;
import com.Project.AppApplication.domain.empresa.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompanyId(Long companyId);
}
