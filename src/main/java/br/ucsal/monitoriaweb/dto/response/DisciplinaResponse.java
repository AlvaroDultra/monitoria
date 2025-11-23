package br.ucsal.monitoriaweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaResponse {

    private Long id;
    private String sigla;
    private String nome;
    private String descricao;
    private String cargaHoraria;
    private String curso;
    private String matrizVinculada;
    private Long escolaId;
    private String escolaNome;
    private Long professorId;
    private String professorNome;
    private Boolean ativo;
}
