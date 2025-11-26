package com.qt.qtBackend.dto.usuario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoUsuarioEnum;
import com.qt.qtBackend.dto.rol.RolAllResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioAllResponse {
    private Long idUsuario;
    private String email;
    private String codigo;
    private String telefono;
    private RolAllResponse rol;
    private EstadoUsuarioEnum estado;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
