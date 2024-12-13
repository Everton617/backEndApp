package com.Project.AppApplication.controller.auth;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.dto.ResponseDTO;
import com.Project.AppApplication.infra.security.TokenService;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins ="*", allowedHeaders = {"Authorization", "Origin","Content-Type","Requestor-Type"},maxAge = 3600 , exposedHeaders =
        {"X-Get-Header","Access-Control-Allow-Origin","Access-Control-Allow-Credentials","Access-Control-Allow-Headers"})
public class AuthController {

    private final CandidatoRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthController(CandidatoRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam("email") String email, @RequestParam("password") String password) {
        Candidato candidato = this.repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(password, candidato.getPassword())) {
            String token = this.tokenService.generateToken(candidato);
            return ResponseEntity.ok(new ResponseDTO(candidato.getNome(),candidato.getId(), token, candidato.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam("name") String name,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password) {
        Optional<Candidato> candidato = this.repository.findByEmail(email);

        if(candidato.isEmpty()) {
            Candidato newUser = new Candidato();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setEmail(email);
            newUser.setNome(name);
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getNome(), newUser.getId(), token, newUser.getRole()));
        }
        return ResponseEntity.badRequest().build();
    }


}