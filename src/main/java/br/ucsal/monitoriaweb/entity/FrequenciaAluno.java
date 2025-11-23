package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "frequencias_alunos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequenciaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private Boolean presente = false;

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro = LocalDateTime.now();

    private String observacao;
}
