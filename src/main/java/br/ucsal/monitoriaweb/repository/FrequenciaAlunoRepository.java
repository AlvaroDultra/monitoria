package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.FrequenciaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FrequenciaAlunoRepository extends JpaRepository<FrequenciaAluno, Long> {

    List<FrequenciaAluno> findByMonitoriaId(Long monitoriaId);

    List<FrequenciaAluno> findByAlunoId(Long alunoId);

    List<FrequenciaAluno> findByMonitoriaIdAndData(Long monitoriaId, LocalDate data);

    Optional<FrequenciaAluno> findByAlunoIdAndMonitoriaIdAndData(Long alunoId, Long monitoriaId, LocalDate data);

    @Query("SELECT COUNT(f) FROM FrequenciaAluno f WHERE f.monitoria.id = :monitoriaId AND f.data = :data AND f.presente = true")
    Long countPresentesByMonitoriaAndData(@Param("monitoriaId") Long monitoriaId, @Param("data") LocalDate data);

    @Query("SELECT COUNT(f) FROM FrequenciaAluno f WHERE f.monitoria.id = :monitoriaId AND f.monitoria.semestre = :semestre AND f.presente = true")
    Long countPresentesByMonitoriaAndSemestre(@Param("monitoriaId") Long monitoriaId, @Param("semestre") Integer semestre);
}
