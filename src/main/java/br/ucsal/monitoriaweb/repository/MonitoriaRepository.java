package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Monitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {

    List<Monitoria> findByDisciplinaId(Long disciplinaId);

    List<Monitoria> findByProfessorId(Long professorId);

    List<Monitoria> findByFinalizada(Boolean finalizada);

    List<Monitoria> findByProfessorIdAndFinalizada(Long professorId, Boolean finalizada);

    List<Monitoria> findBySemestre(Integer semestre);

    List<Monitoria> findByDisciplinaIdAndSemestre(Long disciplinaId, Integer semestre);

    @Query("SELECT COUNT(DISTINCT m) FROM Monitoria mon JOIN mon.monitores m JOIN m.aluno a " +
           "WHERE mon.id = :monitoriaId")
    Long countAlunosByMonitoria(@Param("monitoriaId") Long monitoriaId);

    @Query("SELECT COUNT(DISTINCT m) FROM Monitoria mon JOIN mon.monitores m JOIN m.aluno a " +
           "WHERE mon.disciplina.id = :disciplinaId AND mon.semestre = :semestre")
    Long countAlunosByDisciplinaAndSemestre(@Param("disciplinaId") Long disciplinaId,
                                             @Param("semestre") Integer semestre);
}
