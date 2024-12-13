package com.Project.AppApplication.controller.auth;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Company;
import com.Project.AppApplication.dto.ResponseDTO;
import com.Project.AppApplication.infra.security.company.TokenServiceCompany;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.company.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins ="*", allowedHeaders = {"Authorization", "Origin","Content-Type","Requestor-Type"},maxAge = 3600 , exposedHeaders =
        {"X-Get-Header","Access-Control-Allow-Origin","Access-Control-Allow-Credentials","Access-Control-Allow-Headers"})
public class AuthControllerCompany {

    private final CompanyRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenServiceCompany tokenServiceCompany;

    public AuthControllerCompany(CompanyRepository repository, PasswordEncoder passwordEncoder, TokenServiceCompany tokenServiceCompany) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenServiceCompany = tokenServiceCompany;
    }


    @PostMapping("/company/login")
    public ResponseEntity login(@RequestParam("email") String email, @RequestParam("password") String password) {
        Company company = this.repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(password, company.getPassword())) {
            String token = this.tokenServiceCompany.generateToken(company);
            return ResponseEntity.ok(new ResponseDTO(company.getName(),company.getId(), token, company.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/company/register")
    public ResponseEntity register(@RequestParam("name") String name,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password) {
        Optional<Company> company = this.repository.findByEmail(email);

        if(company.isEmpty()) {
            Company newUser = new Company();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setEmail(email);
            newUser.setName(name);
            this.repository.save(newUser);

            String token = this.tokenServiceCompany.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), newUser.getId(), token, newUser.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }


}
