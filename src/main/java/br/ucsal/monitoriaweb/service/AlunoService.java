package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.AlunoRequest;
import br.ucsal.monitoriaweb.dto.response.AlunoResponse;
import br.ucsal.monitoriaweb.entity.Aluno;
import br.ucsal.monitoriaweb.entity.Disciplina;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.AlunoRepository;
import br.ucsal.monitoriaweb.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Transactional
    public AlunoResponse criar(AlunoRequest request, Long professorId) {
        if (alunoRepository.existsByMatricula(request.getMatricula())) {
            throw new BusinessException("Já existe um aluno com a matrícula: " + request.getMatricula());
        }

        Disciplina disciplina = disciplinaRepository.findById(request.getDisciplinaId())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", request.getDisciplinaId()));

        if (!disciplina.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode cadastrar alunos nas suas próprias disciplinas");
        }

        Aluno aluno = new Aluno();
        aluno.setMatricula(request.getMatricula());
        aluno.setNomeCompleto(request.getNomeCompleto());
        aluno.setDisciplina(disciplina);
        aluno.setProfessor(disciplina.getProfessor());
        aluno.setSemestre(request.getSemestre());
        aluno.setAtivo(true);

        Aluno alunoSalvo = alunoRepository.save(aluno);
        return mapToResponse(alunoSalvo);
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", "id", id));
        return mapToResponse(aluno);
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarPorDisciplina(Long disciplinaId) {
        return alunoRepository.findByDisciplinaId(disciplinaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listarPorProfessor(Long professorId) {
        return alunoRepository.findByProfessorId(professorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request, Long professorId) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", "id", id));

        if (!aluno.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode atualizar alunos cadastrados por você");
        }

        Disciplina disciplina = disciplinaRepository.findById(request.getDisciplinaId())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", request.getDisciplinaId()));

        aluno.setNomeCompleto(request.getNomeCompleto());
        aluno.setDisciplina(disciplina);
        aluno.setSemestre(request.getSemestre());

        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return mapToResponse(alunoAtualizado);
    }

    @Transactional
    public void inativar(Long id, Long professorId) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", "id", id));

        if (!aluno.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode inativar alunos cadastrados por você");
        }

        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    private AlunoResponse mapToResponse(Aluno aluno) {
        AlunoResponse response = new AlunoResponse();
        response.setId(aluno.getId());
        response.setMatricula(aluno.getMatricula());
        response.setNomeCompleto(aluno.getNomeCompleto());
        response.setDisciplinaId(aluno.getDisciplina().getId());
        response.setDisciplinaNome(aluno.getDisciplina().getNome());
        response.setProfessorId(aluno.getProfessor().getId());
        response.setProfessorNome(aluno.getProfessor().getNomeCompleto());
        response.setSemestre(aluno.getSemestre());
        response.setAtivo(aluno.getAtivo());
        return response;
    }
}
