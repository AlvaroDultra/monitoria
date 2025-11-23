package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.MonitoriaRequest;
import br.ucsal.monitoriaweb.dto.response.MonitoriaResponse;
import br.ucsal.monitoriaweb.entity.Disciplina;
import br.ucsal.monitoriaweb.entity.Monitoria;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.DisciplinaRepository;
import br.ucsal.monitoriaweb.repository.MonitoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoriaService {

    private final MonitoriaRepository monitoriaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Transactional
    public MonitoriaResponse criar(MonitoriaRequest request, Long professorId) {
        Disciplina disciplina = disciplinaRepository.findById(request.getDisciplinaId())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina", "id", request.getDisciplinaId()));

        if (!disciplina.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode criar monitorias para suas próprias disciplinas");
        }

        Monitoria monitoria = new Monitoria();
        monitoria.setDisciplina(disciplina);
        monitoria.setProfessor(disciplina.getProfessor());
        monitoria.setTipo(request.getTipo());
        monitoria.setLocal(request.getLocal());
        monitoria.setDataInicio(request.getDataInicio());
        monitoria.setDataEncerramento(request.getDataEncerramento());
        monitoria.setHoraInicio(request.getHoraInicio());
        monitoria.setHoraEncerramento(request.getHoraEncerramento());
        monitoria.setCurso(request.getCurso());
        monitoria.setSemestre(request.getSemestre());
        monitoria.setFinalizada(false);

        Monitoria monitoriaSalva = monitoriaRepository.save(monitoria);
        return mapToResponse(monitoriaSalva);
    }

    @Transactional(readOnly = true)
    public MonitoriaResponse buscarPorId(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", id));
        return mapToResponse(monitoria);
    }

    @Transactional(readOnly = true)
    public List<MonitoriaResponse> listarPorProfessor(Long professorId) {
        return monitoriaRepository.findByProfessorId(professorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MonitoriaResponse> listarEmAndamento(Long professorId) {
        return monitoriaRepository.findByProfessorIdAndFinalizada(professorId, false).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MonitoriaResponse atualizar(Long id, MonitoriaRequest request, Long professorId) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", id));

        if (!monitoria.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode atualizar suas próprias monitorias");
        }

        if (monitoria.getFinalizada()) {
            throw new BusinessException("Não é possível atualizar uma monitoria finalizada");
        }

        monitoria.setTipo(request.getTipo());
        monitoria.setLocal(request.getLocal());
        monitoria.setDataInicio(request.getDataInicio());
        monitoria.setDataEncerramento(request.getDataEncerramento());
        monitoria.setHoraInicio(request.getHoraInicio());
        monitoria.setHoraEncerramento(request.getHoraEncerramento());
        monitoria.setCurso(request.getCurso());
        monitoria.setSemestre(request.getSemestre());

        Monitoria monitoriaAtualizada = monitoriaRepository.save(monitoria);
        return mapToResponse(monitoriaAtualizada);
    }

    @Transactional
    public void finalizar(Long id, Long professorId) {
        Monitoria monitoria = monitoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", id));

        if (!monitoria.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode finalizar suas próprias monitorias");
        }

        if (monitoria.getFinalizada()) {
            throw new BusinessException("Esta monitoria já foi finalizada");
        }

        monitoria.setFinalizada(true);
        monitoria.setDataEncerramento(LocalDate.now());
        monitoriaRepository.save(monitoria);
    }

    @Transactional(readOnly = true)
    public Long contarAlunosPorMonitoria(Long monitoriaId) {
        return monitoriaRepository.countAlunosByMonitoria(monitoriaId);
    }

    private MonitoriaResponse mapToResponse(Monitoria monitoria) {
        MonitoriaResponse response = new MonitoriaResponse();
        response.setId(monitoria.getId());
        response.setDisciplinaId(monitoria.getDisciplina().getId());
        response.setDisciplinaNome(monitoria.getDisciplina().getNome());
        response.setProfessorId(monitoria.getProfessor().getId());
        response.setProfessorNome(monitoria.getProfessor().getNomeCompleto());
        response.setTipo(monitoria.getTipo());
        response.setTipoDescricao(monitoria.getTipo().getDescricao());
        response.setLocal(monitoria.getLocal());
        response.setDataInicio(monitoria.getDataInicio());
        response.setDataEncerramento(monitoria.getDataEncerramento());
        response.setHoraInicio(monitoria.getHoraInicio());
        response.setHoraEncerramento(monitoria.getHoraEncerramento());
        response.setCurso(monitoria.getCurso());
        response.setSemestre(monitoria.getSemestre());
        response.setFinalizada(monitoria.getFinalizada());
        response.setQuantidadeAlunos(monitoriaRepository.countAlunosByMonitoria(monitoria.getId()));
        return response;
    }
}
