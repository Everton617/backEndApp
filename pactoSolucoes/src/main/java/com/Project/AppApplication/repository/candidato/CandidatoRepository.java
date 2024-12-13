package com.Project.AppApplication.repository.candidato;

import com.Project.AppApplication.domain.candidato.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    Optional<Candidato> findByEmail(String email);
}