package br.ucsal.monitoriaweb.dto.request;

import br.ucsal.monitoriaweb.entity.FormacaoProfessor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorRequest {

    @NotBlank(message = "O número de registro é obrigatório")
    private String numeroRegistro;

    @NotBlank(message = "O nome completo é obrigatório")
    private String nomeCompleto;

    @NotNull(message = "A formação é obrigatória")
    private FormacaoProfessor formacao;

    @NotBlank(message = "O nome da instituição é obrigatório")
    private String nomeInstituicao;

    @NotBlank(message = "O nome do curso é obrigatório")
    private String nomeCurso;

    private Integer anoConclusao;

    @NotNull(message = "O ID da escola é obrigatório")
    private Long escolaId;

    @NotBlank(message = "O username é obrigatório")
    private String username;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
