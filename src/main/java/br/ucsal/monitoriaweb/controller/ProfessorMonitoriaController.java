package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.MonitorRequest;
import br.ucsal.monitoriaweb.dto.request.MonitoriaRequest;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.dto.response.MonitoriaResponse;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import br.ucsal.monitoriaweb.service.MonitorService;
import br.ucsal.monitoriaweb.service.MonitoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor/monitorias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorMonitoriaController {

    private final MonitoriaService monitoriaService;
    private final MonitorService monitorService;
    private final ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<MonitoriaResponse> criar(
            @Valid @RequestBody MonitoriaRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        MonitoriaResponse response = monitoriaService.criar(request, professor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MonitoriaResponse>> listarMinhasMonitorias(@AuthenticationPrincipal Usuario usuario) {
        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        List<MonitoriaResponse> monitorias = monitoriaService.listarPorProfessor(professor.getId());
        return ResponseEntity.ok(monitorias);
    }

    @GetMapping("/em-andamento")
    public ResponseEntity<List<MonitoriaResponse>> listarEmAndamento(@AuthenticationPrincipal Usuario usuario) {
        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        List<MonitoriaResponse> monitorias = monitoriaService.listarEmAndamento(professor.getId());
        return ResponseEntity.ok(monitorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponse> buscarPorId(@PathVariable Long id) {
        MonitoriaResponse response = monitoriaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MonitoriaRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        MonitoriaResponse response = monitoriaService.atualizar(id, request, professor.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<MessageResponse> finalizar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        monitoriaService.finalizar(id, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Monitoria finalizada com sucesso"));
    }

    @PostMapping("/associar-aluno")
    public ResponseEntity<MessageResponse> associarAluno(
            @Valid @RequestBody MonitorRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        monitorService.associarAlunoAMonitoria(request, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Aluno associado à monitoria com sucesso"));
    }

    @DeleteMapping("/monitor/{monitorId}")
    public ResponseEntity<MessageResponse> removerAluno(
            @PathVariable Long monitorId,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        monitorService.removerAlunoDeMonitoria(monitorId, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Aluno removido da monitoria com sucesso"));
    }

    @GetMapping("/{id}/quantidade-alunos")
    public ResponseEntity<Long> contarAlunos(@PathVariable Long id) {
        Long quantidade = monitoriaService.contarAlunosPorMonitoria(id);
        return ResponseEntity.ok(quantidade);
    }
}
