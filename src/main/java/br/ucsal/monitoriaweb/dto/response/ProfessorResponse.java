package br.ucsal.monitoriaweb.dto.response;

import br.ucsal.monitoriaweb.entity.FormacaoProfessor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponse {

    private Long id;
    private String numeroRegistro;
    private String nomeCompleto;
    private FormacaoProfessor formacao;
    private String formacaoDescricao;
    private String nomeInstituicao;
    private String nomeCurso;
    private Integer anoConclusao;
    private Long escolaId;
    private String escolaNome;
    private String username;
    private String email;
    private Boolean ativo;
}
