package com.qt.qtBackend.dto.alumno;

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
public class AlumnoShortResponse {
    private Long idAlumno;
    private String nombres;
    private String apellidos;
    private Boolean enabled;
}
