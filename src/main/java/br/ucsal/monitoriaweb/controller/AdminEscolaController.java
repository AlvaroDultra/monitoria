package br.ucsal.monitoriaweb.controller;

import br.ucsal.monitoriaweb.dto.request.EscolaRequest;
import br.ucsal.monitoriaweb.dto.response.EscolaResponse;
import br.ucsal.monitoriaweb.dto.response.MessageResponse;
import br.ucsal.monitoriaweb.service.EscolaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/escolas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEscolaController {

    private final EscolaService escolaService;

    @PostMapping
    public ResponseEntity<EscolaResponse> criar(@Valid @RequestBody EscolaRequest request) {
        EscolaResponse response = escolaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EscolaResponse>> listarTodas() {
        List<EscolaResponse> escolas = escolaService.listarTodas();
        return ResponseEntity.ok(escolas);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<EscolaResponse>> listarAtivas() {
        List<EscolaResponse> escolas = escolaService.listarAtivas();
        return ResponseEntity.ok(escolas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscolaResponse> buscarPorId(@PathVariable Long id) {
        EscolaResponse response = escolaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscolaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EscolaRequest request) {
        EscolaResponse response = escolaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MessageResponse> inativar(@PathVariable Long id) {
        escolaService.inativar(id);
        return ResponseEntity.ok(new MessageResponse("Escola inativada com sucesso"));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<MessageResponse> ativar(@PathVariable Long id) {
        escolaService.ativar(id);
        return ResponseEntity.ok(new MessageResponse("Escola ativada com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletar(@PathVariable Long id) {
        escolaService.deletar(id);
        return ResponseEntity.ok(new MessageResponse("Escola deletada com sucesso"));
    }
}
