package br.ucsal.monitoriaweb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoRequest {

    @NotBlank(message = "A matrícula é obrigatória")
    private String matricula;

    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotNull(message = "O ID da disciplina é obrigatório")
    private Long disciplinaId;

    private Integer semestre;
}
