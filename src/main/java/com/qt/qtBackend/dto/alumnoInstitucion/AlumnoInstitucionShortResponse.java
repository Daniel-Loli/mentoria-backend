package com.qt.qtBackend.dto.alumnoInstitucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.alumno.AlumnoShortResponse;
import com.qt.qtBackend.dto.institucion.InstitucionShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlumnoInstitucionShortResponse {
    private Long idAlumnoInstitucion;
    private AlumnoShortResponse Alumno;
    private InstitucionShortResponse institucion;
    private Boolean enabled;
}
