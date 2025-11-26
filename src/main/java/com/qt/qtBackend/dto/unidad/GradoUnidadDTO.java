package com.qt.qtBackend.dto.unidad;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class GradoUnidadDTO {
    private Integer grado;
    private UnidadIdResponse unidad; // ahora es un solo objeto
}