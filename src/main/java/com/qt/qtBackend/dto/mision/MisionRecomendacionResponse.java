package com.qt.qtBackend.dto.mision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionAllResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.matricula.MatriculaArbolResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MisionRecomendacionResponse {
    AlumnoInstitucionAllResponse alumnoInstitucion;
    ListResponse<MisionIAllResponse> misionesConvocatorio;
    MatriculaArbolResponse matriculaActual;

}
