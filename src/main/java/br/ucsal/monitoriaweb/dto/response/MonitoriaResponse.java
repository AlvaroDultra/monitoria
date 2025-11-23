package br.ucsal.monitoriaweb.dto.response;

import br.ucsal.monitoriaweb.entity.TipoMonitoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoriaResponse {

    private Long id;
    private Long disciplinaId;
    private String disciplinaNome;
    private Long professorId;
    private String professorNome;
    private TipoMonitoria tipo;
    private String tipoDescricao;
    private String local;
    private LocalDate dataInicio;
    private LocalDate dataEncerramento;
    private LocalTime horaInicio;
    private LocalTime horaEncerramento;
    private String curso;
    private Integer semestre;
    private Boolean finalizada;
    private Long quantidadeAlunos;
}
