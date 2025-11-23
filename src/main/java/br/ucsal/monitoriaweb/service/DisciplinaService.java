package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.DisciplinaRequest;
import br.ucsal.monitoriaweb.dto.response.DisciplinaResponse;
import br.ucsal.monitoriaweb.entity.Disciplina;
import br.ucsal.monitoriaweb.entity.Escola;
import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.DisciplinaRepository;
import br.ucsal.monitoriaweb.repository.EscolaRepository;
import br.ucsal.monitoriaweb.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final EscolaRepository escolaRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public DisciplinaResponse criar(DisciplinaRequest request) {
        Escola escola = escolaRepository.findById(request.getEscolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", request.getEscolaId()));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", request.getProfessorId()));

        Disciplina disciplina = new Disciplina();
        disciplina.setSigla(request.getSigla());
        disciplina.setNome(request.getNome());
        disciplina.setDescricao(request.getDescricao());
        disciplina.setCargaHoraria(request.getCargaHoraria());
        disciplina.setCurso(request.getCurso());
        disciplina.setMatrizVinculada(request.getMatrizVinculada());
        disciplina.setEscola(escola);
        disciplina.setProfessor(professor);
        disciplina.setAtivo(true);

        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
        return mapToResponse(disciplinaSalva);
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", id));
        return mapToResponse(disciplina);
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> listarTodas() {
        return disciplinaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> listarPorProfessor(Long professorId) {
        return disciplinaRepository.findByProfessorId(professorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaRequest request) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", id));

        Escola escola = escolaRepository.findById(request.getEscolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola", "id", request.getEscolaId()));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", request.getProfessorId()));

        disciplina.setSigla(request.getSigla());
        disciplina.setNome(request.getNome());
        disciplina.setDescricao(request.getDescricao());
        disciplina.setCargaHoraria(request.getCargaHoraria());
        disciplina.setCurso(request.getCurso());
        disciplina.setMatrizVinculada(request.getMatrizVinculada());
        disciplina.setEscola(escola);
        disciplina.setProfessor(professor);

        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return mapToResponse(disciplinaAtualizada);
    }

    @Transactional
    public void inativar(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", id));
        disciplina.setAtivo(false);
        disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void ativar(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", id));
        disciplina.setAtivo(true);
        disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void deletar(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", id));
        disciplinaRepository.delete(disciplina);
    }

    private DisciplinaResponse mapToResponse(Disciplina disciplina) {
        DisciplinaResponse response = new DisciplinaResponse();
        response.setId(disciplina.getId());
        response.setSigla(disciplina.getSigla());
        response.setNome(disciplina.getNome());
        response.setDescricao(disciplina.getDescricao());
        response.setCargaHoraria(disciplina.getCargaHoraria());
        response.setCurso(disciplina.getCurso());
        response.setMatrizVinculada(disciplina.getMatrizVinculada());
        response.setEscolaId(disciplina.getEscola().getId());
        response.setEscolaNome(disciplina.getEscola().getNome());
        response.setProfessorId(disciplina.getProfessor().getId());
        response.setProfessorNome(disciplina.getProfessor().getNomeCompleto());
        response.setAtivo(disciplina.getAtivo());
        return response;
    }
}
