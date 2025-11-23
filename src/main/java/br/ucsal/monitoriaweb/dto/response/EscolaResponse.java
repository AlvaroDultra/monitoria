package br.ucsal.monitoriaweb.dto.response;

import br.ucsal.monitoriaweb.entity.TipoEscola;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscolaResponse {

    private Long id;
    private String nome;
    private TipoEscola tipo;
    private String tipoDescricao;
    private Boolean ativo;

    public EscolaResponse(Long id, String nome, TipoEscola tipo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.tipoDescricao = tipo.getDescricao();
        this.ativo = ativo;
    }
}
