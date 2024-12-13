package com.Project.AppApplication.infra.security.company;


import com.Project.AppApplication.domain.empresa.Company;
import com.Project.AppApplication.repository.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomCompanyDetailsService implements UserDetailsService {
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Company company = this.companyRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(company.getEmail(), company.getPassword(), new ArrayList<>());
    }
}