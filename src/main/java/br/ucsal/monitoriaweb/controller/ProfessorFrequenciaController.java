package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.FrequenciaRequest;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import br.ucsal.monitoriaweb.service.FrequenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professor/frequencias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorFrequenciaController {

    private final FrequenciaService frequenciaService;
    private final ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<MessageResponse> registrarFrequencia(
            @Valid @RequestBody FrequenciaRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        frequenciaService.registrarFrequencia(request, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Frequência registrada com sucesso"));
    }

    @PatchMapping("/{frequenciaId}")
    public ResponseEntity<MessageResponse> atualizarFrequencia(
            @PathVariable Long frequenciaId,
            @RequestParam Boolean presente,
            @AuthenticationPrincipal Usuario usuario) {

        Professor professor = professorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        frequenciaService.atualizarFrequencia(frequenciaId, presente, professor.getId());
        return ResponseEntity.ok(new MessageResponse("Frequência atualizada com sucesso"));
    }
}
