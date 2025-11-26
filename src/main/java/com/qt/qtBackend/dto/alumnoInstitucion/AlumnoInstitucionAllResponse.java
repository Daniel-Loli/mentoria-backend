package com.qt.qtBackend.dto.alumnoInstitucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoAlumnoInstitucion;
import com.qt.qtBackend.dto.alumno.AlumnoShortResponse;
import com.qt.qtBackend.dto.institucion.InstitucionShortResponse;
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
public class AlumnoInstitucionAllResponse {
    private Long idAlumnoInstitucion;
    private AlumnoShortResponse Alumno;
    private InstitucionShortResponse institucion;
    private EstadoAlumnoInstitucion estado;
    private ZonedDateTime createdAt;
    private ZonedDateTime updateAt;
    private Boolean enabled;
}
