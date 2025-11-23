package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    List<Monitor> findByMonitoriaId(Long monitoriaId);

    List<Monitor> findByAlunoId(Long alunoId);

    List<Monitor> findByMonitoriaIdAndAtivo(Long monitoriaId, Boolean ativo);

    Optional<Monitor> findByAlunoIdAndMonitoriaId(Long alunoId, Long monitoriaId);

    Boolean existsByAlunoIdAndMonitoriaId(Long alunoId, Long monitoriaId);
}
