package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A sigla da disciplina é obrigatória")
    @Column(nullable = false)
    private String sigla;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @NotBlank(message = "A carga horária é obrigatória")
    @Column(nullable = false)
    private String cargaHoraria;

    @NotBlank(message = "O curso é obrigatório")
    @Column(nullable = false)
    private String curso;

    @NotBlank(message = "A matriz vinculada é obrigatória")
    @Column(nullable = false)
    private String matrizVinculada;

    @ManyToOne
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Monitoria> monitorias = new ArrayList<>();
}
