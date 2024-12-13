package com.Project.AppApplication.controller;

import com.Project.AppApplication.domain.candidato.Candidato;
import com.Project.AppApplication.repository.candidato.CandidatoRepository;
import com.Project.AppApplication.repository.candidato.ExperienciaRepository;
import com.Project.AppApplication.repository.candidato.HabilidadeRepository;
import com.Project.AppApplication.services.CandidatoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidatos")
@CrossOrigin(origins ="*", allowedHeaders = {"Authorization", "Origin","Content-Type","Requestor-Type"},maxAge = 3600 , exposedHeaders =
        {"X-Get-Header","Access-Control-Allow-Origin","Access-Control-Allow-Credentials","Access-Control-Allow-Headers"})
public class CandidatoController {
    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private ExperienciaRepository experienciaRepository; // Injeção de dependência

    @Autowired
    private HabilidadeRepository habilidadesRepository; // Injeção de dependência para habilidades

    @Autowired
    private CandidatoRepository candidatoRepository;

    @GetMapping
    public List<Candidato> listarTodos() {
        return candidatoService.listarCandidatos();
    }

    @PostMapping
    public ResponseEntity<Candidato> criarCandidato(@RequestBody Candidato candidato) {
        Candidato novoCandidato = candidatoService.salvarCandidato(candidato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCandidato);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidato> buscarCandidatoPorId(@PathVariable Long id) {
        // Chama o serviço para buscar o candidato
        Optional<Candidato> candidatoOpt = candidatoService.buscarCandidatoPorId(id);

        // Se o candidato for encontrado e o formulário estiver preenchido
        if (candidatoOpt.isPresent()) {
            return ResponseEntity.ok(candidatoOpt.get());  // Retorna o candidato encontrado
        } else {
            // Retorna 404 se não encontrado ou o formulário não estiver preenchido
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCandidato(@PathVariable Long id) {
        candidatoService.deletarCandidato(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidato> atualizarCandidato(
            @PathVariable Long id,
            @RequestBody Candidato candidatoAtualizado) {

        try {
            // Chama o serviço para atualizar o candidato
            Candidato candidato = candidatoService.atualizarCandidato(id, candidatoAtualizado);
            return ResponseEntity.ok(candidato);  // Retorna o candidato atualizado com status 200 OK
        } catch (EntityNotFoundException e) {
            // Caso o candidato não seja encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Retorna status 404 com corpo vazio
        } catch (Exception e) {
            // Qualquer outro erro genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);  // Retorna status 500 com corpo vazio
        }
    }


}