package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @NotNull(message = "O tipo de monitoria é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMonitoria tipo;

    private String local;

    @NotNull(message = "A data de início é obrigatória")
    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataEncerramento;

    @NotNull(message = "O horário de início é obrigatório")
    @Column(nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "O horário de encerramento é obrigatório")
    @Column(nullable = false)
    private LocalTime horaEncerramento;

    @NotNull(message = "O curso é obrigatório")
    @Column(nullable = false)
    private String curso;

    @NotNull(message = "O semestre é obrigatório")
    @Column(nullable = false)
    private Integer semestre;

    @Column(nullable = false)
    private Boolean finalizada = false;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<Monitor> monitores = new ArrayList<>();

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<AssuntoMonitoria> assuntos = new ArrayList<>();

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private List<FrequenciaAluno> frequencias = new ArrayList<>();
}
