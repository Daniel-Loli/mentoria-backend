package com.qt.qtBackend.dto.institucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstitucionShortResponse {
    private Long idInstitucion;
    //private UsuarioShortResponse usuario;
    private String nombre;
    private String numero;
    private Boolean enabled;
}
