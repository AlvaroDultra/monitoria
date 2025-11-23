package br.ucsal.monitoriaweb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponse {

    private Long id;
    private String matricula;
    private String nomeCompleto;
    private Long disciplinaId;
    private String disciplinaNome;
    private Long professorId;
    private String professorNome;
    private Integer semestre;
    private Boolean ativo;
}
