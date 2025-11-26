package com.qt.qtBackend.dto.unidadAsignatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.asignatura.AsignaturaShortResponse;
import com.qt.qtBackend.dto.unidad.UnidadShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnidadAsignaturaAllResponse {
    private Long idUnidadAsignatura;
    private UnidadShortResponse unidad;
    private AsignaturaShortResponse asignatura;
}
