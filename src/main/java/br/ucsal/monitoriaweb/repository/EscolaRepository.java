package br.ucsal.monitoriaweb.repository;

import br.ucsal.monitoriaweb.entity.Escola;
import br.ucsal.monitoriaweb.entity.TipoEscola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {

    List<Escola> findByAtivo(Boolean ativo);

    List<Escola> findByTipo(TipoEscola tipo);

    List<Escola> findByTipoAndAtivo(TipoEscola tipo, Boolean ativo);
}
