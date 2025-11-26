package com.qt.qtBackend.dto.unidad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.NivelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnidadIdResponse {
    private Long idUnidad;
    private Boolean enabled;
}
