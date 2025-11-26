package com.qt.qtBackend.dto.matricula;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import com.qt.qtBackend.Enum.NivelEnum;
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
public class MatriculaShortResponse {
    private Long idMatricula;
    private Integer anio;
    private NivelEnum nivel;
    private Integer grado;
    private EstadoMatriculaEnum estado;
}
