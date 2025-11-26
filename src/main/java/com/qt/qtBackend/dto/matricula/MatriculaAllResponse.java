package com.qt.qtBackend.dto.matricula;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionShortResponse;
import com.qt.qtBackend.model.AlumnoInstitucion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatriculaAllResponse {
    private Long idMatricula;
    private AlumnoInstitucionShortResponse alumnoInstitucion;
    private Integer anio;
    private NivelEnum nivel;
    private Integer grado;
    private EstadoMatriculaEnum estado;
}
