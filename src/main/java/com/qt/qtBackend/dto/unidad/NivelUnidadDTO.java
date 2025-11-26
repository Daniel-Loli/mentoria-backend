package com.qt.qtBackend.dto.unidad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.NivelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NivelUnidadDTO {
    private NivelEnum nivel;
    private List<GradoUnidadDTO> grados;
}