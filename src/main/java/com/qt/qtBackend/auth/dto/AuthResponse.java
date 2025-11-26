package com.qt.qtBackend.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse{
    private String token;
    private UsuarioAllResponse usuario;
    private UgelAuthResponse ugel;
    private InstitucionAuthResponse institucion;
    private DocenteAuthResponse docente;
    private AlumnoAuthResponse alumno;
}
