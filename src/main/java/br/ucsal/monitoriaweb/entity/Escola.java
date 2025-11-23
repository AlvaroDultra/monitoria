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
@Table(name = "escolas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Escola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da escola é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O tipo da escola é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEscola tipo;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL)
    private List<Disciplina> disciplinas = new ArrayList<>();
}
