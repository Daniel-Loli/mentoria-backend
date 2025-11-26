package com.qt.qtBackend.dto.unidad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.NivelEnum;
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
public class UnidadAllResponse {
    private Long idUnidad;
    private NivelEnum nivel;
    private Integer grado;
    private InstitucionShortResponse institucion;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;
}
