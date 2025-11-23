package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.DisciplinaRequest;
import br.ucsal.monitoriaweb.dto.response.DisciplinaResponse;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.service.DisciplinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/disciplinas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDisciplinaController {

    private final DisciplinaService disciplinaService;

    @PostMapping
    public ResponseEntity<DisciplinaResponse> criar(@Valid @RequestBody DisciplinaRequest request) {
        DisciplinaResponse response = disciplinaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaResponse>> listarTodas() {
        List<DisciplinaResponse> disciplinas = disciplinaService.listarTodas();
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> buscarPorId(@PathVariable Long id) {
        DisciplinaResponse response = disciplinaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DisciplinaRequest request) {
        DisciplinaResponse response = disciplinaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MessageResponse> inativar(@PathVariable Long id) {
        disciplinaService.inativar(id);
        return ResponseEntity.ok(new MessageResponse("Disciplina inativada com sucesso"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<MessageResponse> ativar(@PathVariable Long id) {
        disciplinaService.ativar(id);
        return ResponseEntity.ok(new MessageResponse("Disciplina ativada com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        disciplinaService.deletar(id);
        return ResponseEntity.ok(new MessageResponse("Disciplina deletada com sucesso"));
    }
}
