package br.ucsal.monitoriaweb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaRequest {

    @NotBlank(message = "A sigla da disciplina é obrigatória")
    private String sigla;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nome;

    private String descricao;

    @NotBlank(message = "A carga horária é obrigatória")
    private String cargaHoraria;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @NotBlank(message = "A matriz vinculada é obrigatória")
    private String matrizVinculada;

    @NotNull(message = "O ID da escola é obrigatório")
    private Long escolaId;

    @NotNull(message = "O ID do professor é obrigatório")
    private Long professorId;
}
