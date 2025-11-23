package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "monitoria_id", nullable = false)
    private Monitoria monitoria;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL)
    private List<FrequenciaAluno> frequenciasRegistradas = new ArrayList<>();
}
