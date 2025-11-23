package br.ucsal.monitoriaweb.service;

import br.ucsal.monitoriaweb.dto.request.AssuntoMonitoriaRequest;
import br.ucsal.monitoriaweb.entity.AssuntoMonitoria;
import br.ucsal.monitoriaweb.entity.Monitoria;
import br.ucsal.monitoriaweb.exception.BusinessException;
import br.ucsal.monitoriaweb.exception.ResourceNotFoundException;
import br.ucsal.monitoriaweb.repository.AssuntoMonitoriaRepository;
import br.ucsal.monitoriaweb.repository.MonitoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssuntoMonitoriaService {

    private final AssuntoMonitoriaRepository assuntoRepository;
    private final MonitoriaRepository monitoriaRepository;

    @Transactional
    public AssuntoMonitoria registrarAssunto(AssuntoMonitoriaRequest request, Long professorId) {
        Monitoria monitoria = monitoriaRepository.findById(request.getMonitoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria", "id", request.getMonitoriaId()));

        if (!monitoria.getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode registrar assuntos nas suas próprias monitorias");
        }

        if (monitoria.getFinalizada()) {
            throw new BusinessException("Não é possível registrar assuntos em uma monitoria finalizada");
        }

        AssuntoMonitoria assunto = new AssuntoMonitoria();
        assunto.setMonitoria(monitoria);
        assunto.setData(request.getData());
        assunto.setAssunto(request.getAssunto());
        assunto.setAtividadePraticaAplicada(request.getAtividadePraticaAplicada());
        assunto.setDataHoraRegistro(LocalDateTime.now());

        return assuntoRepository.save(assunto);
    }

    @Transactional(readOnly = true)
    public List<AssuntoMonitoria> listarPorMonitoria(Long monitoriaId) {
        return assuntoRepository.findByMonitoriaIdOrderByDataDesc(monitoriaId);
    }

    @Transactional
    public void excluir(Long assuntoId, Long professorId) {
        AssuntoMonitoria assunto = assuntoRepository.findById(assuntoId)
                .orElseThrow(() -> new ResourceNotFoundException("Assunto", "id", assuntoId));

        if (!assunto.getMonitoria().getProfessor().getId().equals(professorId)) {
            throw new BusinessException("Você só pode excluir assuntos das suas próprias monitorias");
        }

        assuntoRepository.delete(assunto);
    }
}
