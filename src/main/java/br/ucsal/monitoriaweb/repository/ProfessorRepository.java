package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Professor;
import br.ucsal.monitoriaweb.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByNumeroRegistro(String numeroRegistro);

    Optional<Professor> findByUsuario(Usuario usuario);

    List<Professor> findByAtivo(Boolean ativo);

    List<Professor> findByEscolaId(Long escolaId);

    Boolean existsByNumeroRegistro(String numeroRegistro);
}
