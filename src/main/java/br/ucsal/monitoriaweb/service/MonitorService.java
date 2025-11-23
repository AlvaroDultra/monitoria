package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.MonitorRequest;
import br.ucsal.monitoriaweb.entity.Aluno;
import br.ucsal.monitoriaweb.entity.Monitor;
import br.ucsal.monitoriaweb.entity.Monitoria;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.AlunoRepository;
import br.ucsal.monitoriaweb.repository.MonitorRepository;
import br.ucsal.monitoriaweb.repository.MonitoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final AlunoRepository alunoRepository;
    private final MonitoriaRepository monitoriaRepository;

    @Transactional
    public void associarAlunoAMonitoria(MonitorRequest request, Long professorId) {
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", "id", request.getAlunoId()));

        Monitoria monitoria = monitoriaRepository.findById(request.getMonitoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", request.getMonitoriaId()));

        if (!monitoria.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode associar alunos às suas próprias monitorias");
        }

        if (monitoria.getFinalizada()) {
            throw new BusinessException("Não é possível associar alunos a uma monitoria finalizada");
        }

        if (monitorRepository.existsByAlunoIdAndMonitoriaId(request.getAlunoId(), request.getMonitoriaId())) {
            throw new BusinessException("Este aluno já está associado a esta monitoria");
        }

        Monitor monitor = new Monitor();
        monitor.setAluno(aluno);
        monitor.setMonitoria(monitoria);
        monitor.setAtivo(true);

        monitorRepository.save(monitor);
    }

    @Transactional
    public void removerAlunoDeMonitoria(Long monitorId, Long professorId) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor", "id", monitorId));

        if (!monitor.getMonitoria().getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode remover alunos das suas próprias monitorias");
        }

        monitor.setAtivo(false);
        monitorRepository.save(monitor);
    }
}
