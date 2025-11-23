package br.ucsal.monitoriaweb.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequenciaRequest {

    @NotNull(message = "O ID do aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "O ID da monitoria é obrigatório")
    private Long monitoriaId;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @NotNull(message = "O status de presença é obrigatório")
    private Boolean presente;

    private String observacao;
}
