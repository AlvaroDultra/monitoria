package br.ucsal.monitoriaweb.dto.request;

import br.ucsal.monitoriaweb.entity.TipoMonitoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoriaRequest {

    @NotNull(message = "O ID da disciplina é obrigatório")
    private Long disciplinaId;

    @NotNull(message = "O tipo de monitoria é obrigatório")
    private TipoMonitoria tipo;

    private String local;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataEncerramento;

    @NotNull(message = "O horário de início é obrigatório")
    private LocalTime horaInicio;

    @NotNull(message = "O horário de encerramento é obrigatório")
    private LocalTime horaEncerramento;

    @NotBlank(message = "O curso é obrigatório")
    private String curso;

    @NotNull(message = "O semestre é obrigatório")
    private Integer semestre;
}
