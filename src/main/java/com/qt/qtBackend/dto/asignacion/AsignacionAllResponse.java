package com.qt.qtBackend.dto.asignacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoAsignacionEnum;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionShortResponse;
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
public class AsignacionAllResponse {
    private Long idAsignacion;
    //private AlumnoInstitucionShortResponse alumno;
    private EstadoAsignacionEnum estado;
    private Integer puntos;
    private String evidencia;
    private String respuesta;
    private Boolean isCompletado;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;
}
