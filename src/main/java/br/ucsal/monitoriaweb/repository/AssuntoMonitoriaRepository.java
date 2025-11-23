package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.AssuntoMonitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssuntoMonitoriaRepository extends JpaRepository<AssuntoMonitoria, Long> {

    List<AssuntoMonitoria> findByMonitoriaId(Long monitoriaId);

    List<AssuntoMonitoria> findByMonitoriaIdAndData(Long monitoriaId, LocalDate data);

    List<AssuntoMonitoria> findByMonitoriaIdOrderByDataDesc(Long monitoriaId);
}
