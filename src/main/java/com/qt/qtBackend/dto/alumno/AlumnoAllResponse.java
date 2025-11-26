package com.qt.qtBackend.dto.alumno;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoAlumnoEnum;
import com.qt.qtBackend.Enum.EstadoDocenteEnum;
import com.qt.qtBackend.Enum.SexoEnum;
import com.qt.qtBackend.Enum.TipoDocumentoEnum;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
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
public class AlumnoAllResponse {
    private Long idAlumno;
    private String nombres;
    private String apellidos;
    private UsuarioShortResponse usuario;
    private TipoDocumentoEnum tipoDocumento;
    private String docIdentidad;
    private SexoEnum sexo;
    private EstadoAlumnoEnum estado;
    private ZonedDateTime createdAt;
    private ZonedDateTime updateAt;
    private Boolean enabled;
}
