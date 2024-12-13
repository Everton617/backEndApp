package com.Project.AppApplication.repository.company;

import com.Project.AppApplication.domain.candidato.Experiencia;
import com.Project.AppApplication.domain.empresa.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
