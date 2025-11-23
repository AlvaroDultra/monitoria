package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.ProfessorRequest;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.dto.response.ProfessorResponse;
import br.ucsal.monitoriaweb.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/professores")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfessorController {

    private final ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(@Valid @RequestBody ProfessorRequest request) {
        ProfessorResponse response = professorService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> listarTodos() {
        List<ProfessorResponse> professores = professorService.listarTodos();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProfessorResponse>> listarAtivos() {
        List<ProfessorResponse> professores = professorService.listarAtivos();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> buscarPorId(@PathVariable Long id) {
        ProfessorResponse response = professorService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MessageResponse> inativar(@PathVariable Long id) {
        professorService.inativar(id);
        return ResponseEntity.ok(new MessageResponse("Professor inativado com sucesso"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<MessageResponse> ativar(@PathVariable Long id) {
        professorService.ativar(id);
        return ResponseEntity.ok(new MessageResponse("Professor ativado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        professorService.deletar(id);
        return ResponseEntity.ok(new MessageResponse("Professor deletado com sucesso"));
    }
}
