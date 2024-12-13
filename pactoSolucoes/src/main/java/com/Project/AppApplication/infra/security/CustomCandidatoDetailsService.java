package com.Project.AppApplication.infra.security;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomCandidatoDetailsService implements UserDetailsService {
    @Autowired
    private CandidatoRepository candidatoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Candidato candidato = this.candidatoRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(candidato.getEmail(), candidato.getPassword(), new ArrayList<>());
    }
}