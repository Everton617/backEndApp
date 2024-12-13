package com.Project.AppApplication.repository.company;

import com.Project.AppApplication.domain.empresa.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
}
