package br.ucsal.monitoriaweb.dto.request;

import br.ucsal.monitoriaweb.entity.TipoEscola;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscolaRequest {

    @NotBlank(message = "O nome da escola é obrigatório")
    private String nome;

    @NotNull(message = "O tipo da escola é obrigatório")
    private TipoEscola tipo;

    private Boolean ativo = true;
}
