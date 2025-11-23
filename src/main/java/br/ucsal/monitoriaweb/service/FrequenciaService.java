package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.FrequenciaRequest;
import br.ucsal.monitoriaweb.entity.Aluno;
import br.ucsal.monitoriaweb.entity.FrequenciaAluno;
import br.ucsal.monitoriaweb.entity.Monitor;
import br.ucsal.monitoriaweb.entity.Monitoria;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.AlunoRepository;
import br.ucsal.monitoriaweb.repository.FrequenciaAlunoRepository;
import br.ucsal.monitoriaweb.repository.MonitorRepository;
import br.ucsal.monitoriaweb.repository.MonitoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FrequenciaService {

    private final FrequenciaAlunoRepository frequenciaRepository;
    private final AlunoRepository alunoRepository;
    private final MonitoriaRepository monitoriaRepository;
    private final MonitorRepository monitorRepository;

    @Transactional
    public void registrarFrequencia(FrequenciaRequest request, Long professorId) {
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", "id", request.getAlunoId()));

        Monitoria monitoria = monitoriaRepository.findById(request.getMonitoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", request.getMonitoriaId()));

        if (!monitoria.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode registrar frequência nas suas próprias monitorias");
        }

        if (monitoria.getFinalizada()) {
            throw new BusinessException("Não é possível registrar frequência em uma monitoria finalizada");
        }

        // Verificar se o aluno está associado à monitoria
        Monitor monitor = monitorRepository.findByAlunoIdAndMonitoriaId(request.getAlunoId(), request.getMonitoriaId())
                .orElseThrow(() -> new BusinessException("Este aluno não está associado a esta monitoria"));

        // Verificar se já existe registro de frequência para este aluno nesta data
        frequenciaRepository.findByAlunoIdAndMonitoriaIdAndData(
                request.getAlunoId(),
                request.getMonitoriaId(),
                request.getData()
        ).ifPresent(f -> {
            throw new BusinessException("Já existe um registro de frequência para este aluno nesta data");
        });

        FrequenciaAluno frequencia = new FrequenciaAluno();
        frequencia.setAluno(aluno);
        frequencia.setMonitoria(monitoria);
        frequencia.setMonitor(monitor);
        frequencia.setData(request.getData());
        frequencia.setPresente(request.getPresente());
        frequencia.setObservacao(request.getObservacao());
        frequencia.setDataHoraRegistro(LocalDateTime.now());

        frequenciaRepository.save(frequencia);
    }

    @Transactional
    public void atualizarFrequencia(Long frequenciaId, Boolean presente, Long professorId) {
        FrequenciaAluno frequencia = frequenciaRepository.findById(frequenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Frequência", "id", frequenciaId));

        if (!frequencia.getMonitoria().getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode atualizar frequências das suas próprias monitorias");
        }

        frequencia.setPresente(presente);
        frequenciaRepository.save(frequencia);
    }
}
