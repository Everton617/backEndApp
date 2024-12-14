package com.Project.AppApplication.controller.auth;

import com.Project.AppApplication.domain.LoginUser;
import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.domain.empresa.Company;
import com.Project.AppApplication.dto.ResponseDTO;
import com.Project.AppApplication.infra.security.TokenService;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.company.CompanyRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins ="*", allowedHeaders = {"Authorization", "Origin","Content-Type","Requestor-Type"},maxAge = 3600 , exposedHeaders =
        {"X-Get-Header","Access-Control-Allow-Origin","Access-Control-Allow-Credentials","Access-Control-Allow-Headers"})
public class AuthController {

    private final CandidatoRepository repository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthController(CandidatoRepository repository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam("userType") String userType,@RequestParam("email") String email, @RequestParam("password") String password) {
        Candidato candidato = this.repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        LoginUser user;
        if (userType.equalsIgnoreCase("candidato")) {
            user = this.repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Candidato not found"));
        } else if (userType.equalsIgnoreCase("company")) {
            user = this.companyRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Company not found"));
        } else {
            return ResponseEntity.badRequest().body("Invalid user type");
        }

        if(passwordEncoder.matches(password, candidato.getPassword())) {
            String token = this.tokenService.generateToken(candidato);
            return ResponseEntity.ok(new ResponseDTO(candidato.getNome(),candidato.getId(), token, candidato.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("userType") String userType // Add userType parameter
    ) {
        if (userType.equalsIgnoreCase("candidato")) {
            Optional<Candidato> existingCandidato = this.repository.findByEmail(email);
            if (existingCandidato.isEmpty()) {
                Candidato newUser = new Candidato();
                newUser.setPassword(passwordEncoder.encode(password));
                newUser.setEmail(email);
                newUser.setNome(name);
                // ... set other fields for Candidato ...
                this.repository.save(newUser);
                String token = this.tokenService.generateToken(newUser);
                return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), newUser.getId(), token, newUser.getRole()));
            } else {
                return ResponseEntity.badRequest().body("Candidato with this email already exists"); // More specific error message
            }

        } else if (userType.equalsIgnoreCase("company")) {
            Optional<Company> existingCompany = this.companyRepository.findByEmail(email);
            if (existingCompany.isEmpty()) {
                Company newCompany = new Company();
                newCompany.setPassword(passwordEncoder.encode(password));
                newCompany.setEmail(email);
                newCompany.setName(name); // Or companyName, adjust accordingly
                // ... set other fields for Company ...
                this.companyRepository.save(newCompany);
                String token = this.tokenService.generateToken(newCompany);
                return ResponseEntity.ok(new ResponseDTO(newCompany.getNome(), newCompany.getId(), token, newCompany.getRole()));
            } else {
                return ResponseEntity.badRequest().body("Company with this email already exists"); // More specific error message
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid user type");
        }
    }


}
