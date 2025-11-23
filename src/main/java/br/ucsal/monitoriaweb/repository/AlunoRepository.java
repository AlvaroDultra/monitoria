package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByMatricula(String matricula);

    List<Aluno> findByDisciplinaId(Long disciplinaId);

    List<Aluno> findByProfessorId(Long professorId);

    List<Aluno> findByDisciplinaIdAndAtivo(Long disciplinaId, Boolean ativo);

    List<Aluno> findByAtivo(Boolean ativo);

    Boolean existsByMatricula(String matricula);
}
