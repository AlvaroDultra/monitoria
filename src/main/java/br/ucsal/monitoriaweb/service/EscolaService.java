package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.EscolaRequest;
import br.ucsal.monitoriaweb.dto.response.EscolaResponse;
import br.ucsal.monitoriaweb.entity.Escola;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;

    @Transactional
    public EscolaResponse criar(EscolaRequest request) {
        Escola escola = new Escola();
        escola.setNome(request.getNome());
        escola.setTipo(request.getTipo());
        escola.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

        Escola escolaSalva = escolaRepository.save(escola);
        return mapToResponse(escolaSalva);
    }

    @Transactional(readOnly = true)
    public EscolaResponse buscarPorId(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", id));
        return mapToResponse(escola);
    }

    @Transactional(readOnly = true)
    public List<EscolaResponse> listarTodas() {
        return escolaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EscolaResponse> listarAtivas() {
        return escolaRepository.findByAtivo(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EscolaResponse atualizar(Long id, EscolaRequest request) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", id));

        escola.setNome(request.getNome());
        escola.setTipo(request.getTipo());
        if (request.getAtivo() != null) {
            escola.setAtivo(request.getAtivo());
        }

        Escola escolaAtualizada = escolaRepository.save(escola);
        return mapToResponse(escolaAtualizada);
    }

    @Transactional
    public void inativar(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", id));
        escola.setAtivo(false);
        escolaRepository.save(escola);
    }

    @Transactional
    public void ativar(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", id));
        escola.setAtivo(true);
        escolaRepository.save(escola);
    }

    @Transactional
    public void deletar(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", id));
        escolaRepository.delete(escola);
    }

    private EscolaResponse mapToResponse(Escola escola) {
        return new EscolaResponse(
                escola.getId(),
                escola.getNome(),
                escola.getTipo(),
                escola.getAtivo()
        );
    }
}
