package com.qt.qtBackend.dto.asignacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoAsignacionEnum;
import com.qt.qtBackend.dto.alumno.AlumnoShortResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoIShortResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsignacionFilterAShortResponse {
    private Long idAsignacion;
    private AlumnoInstitucionShortResponse alumno;
    private EstadoAsignacionEnum estado;
    private Boolean enabled;
}
