package br.ucsal.monitoriaweb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssuntoMonitoriaRequest {

    @NotNull(message = "O ID da monitoria é obrigatório")
    private Long monitoriaId;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @NotBlank(message = "O assunto é obrigatório")
    private String assunto;

    private String atividadePraticaAplicada;
}
