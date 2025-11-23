package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assuntos_monitoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssuntoMonitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank(message = "O assunto é obrigatório")
    @Column(nullable = false, length = 1000)
    private String assunto;

    @Column(length = 2000)
    private String atividadePraticaAplicada;

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro = LocalDateTime.now();
}
