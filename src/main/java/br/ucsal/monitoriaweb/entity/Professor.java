package br.ucsal.monitoriaweb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O número de registro é obrigatório")
    @Column(unique = true, nullable = false)
    private String numeroRegistro;

    @NotBlank(message = "O nome completo é obrigatório")
    @Column(nullable = false)
    private String nomeCompleto;

    @NotNull(message = "A formação é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormacaoProfessor formacao;

    @NotBlank(message = "O nome da instituição é obrigatório")
    @Column(nullable = false)
    private String nomeInstituicao;

    @NotBlank(message = "O nome do curso é obrigatório")
    @Column(nullable = false)
    private String nomeCurso;

    private Integer anoConclusao;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Disciplina> disciplinas = new ArrayList<>();
}
