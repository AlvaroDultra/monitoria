package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.AssuntoMonitoriaRequest;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.entity.AssuntoMonitoria;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import br.ucsal.monitoriaweb.service.AssuntoMonitoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor/assuntos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorAssuntoController {

    private final AssuntoMonitoriaService assuntoService;
    private final ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<AssuntoMonitoria> registrarAssunto(
            @Valid @RequestBody AssuntoMonitoriaRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        AssuntoMonitoria assunto = assuntoService.registrarAssunto(request, professor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(assunto);
    }

    @GetMapping("/monitoria/{monitoriaId}")
    public ResponseEntity<List<AssuntoMonitoria>> listarPorMonitoria(@PathVariable Long monitoriaId) {
        List<AssuntoMonitoria> assuntos = assuntoService.listarPorMonitoria(monitoriaId);
        return ResponseEntity.ok(assuntos);
    }

    @DeleteMapping("/{assuntoId}")
    public ResponseEntity<MessageResponse> excluir(
            @PathVariable Long assuntoId,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        assuntoService.excluir(assuntoId, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Assunto excluído com sucesso"));
    }
}
