package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findByAtivo(Boolean ativo);

    List<Disciplina> findByEscolaId(Long escolaId);

    List<Disciplina> findByProfessorId(Long professorId);

    List<Disciplina> findByProfessorIdAndAtivo(Long professorId, Boolean ativo);

    List<Disciplina> findByCurso(String curso);

    List<Disciplina> findByMatrizVinculada(String matrizVinculada);
}
