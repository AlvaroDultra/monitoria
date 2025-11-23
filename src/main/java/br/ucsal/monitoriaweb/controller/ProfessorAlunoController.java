package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.AlunoRequest;
import br.ucsal.monitoriaweb.dto.response.AlunoResponse;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import br.ucsal.monitoriaweb.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor/alunos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorAlunoController {

    private final AlunoService alunoService;
    private final ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<AlunoResponse> criar(
            @Valid @RequestBody AlunoRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        AlunoResponse response = alunoService.criar(request, professor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listarMeusAlunos(@AuthenticationPrincipal Usuario usuario) {
        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        List<AlunoResponse> alunos = alunoService.listarPorProfessor(professor.getId());
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/disciplina/{disciplinaId}")
    public ResponseEntity<List<AlunoResponse>> listarPorDisciplina(@PathVariable Long disciplinaId) {
        List<AlunoResponse> alunos = alunoService.listarPorDisciplina(disciplinaId);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarPorId(@PathVariable Long id) {
        AlunoResponse response = alunoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        AlunoResponse response = alunoService.atualizar(id, request, professor.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MessageResponse> inativar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        alunoService.inativar(id, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Aluno inativado com sucesso"));
    }
}
