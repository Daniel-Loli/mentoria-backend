package com.qt.qtBackend.dto.unidad;

import com.qt.qtBackend.Enum.NivelEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnidadCreateRequest {

    @NotNull(message = "El nivel no puede ser nulo")
    private NivelEnum nivel;

    @NotNull(message = "El grado no puede ser nulo")
    @Min(value = 1, message = "El grado mínimo debe ser 1")
    @Max(value = 12, message = "El grado máximo debe ser 12")
    private Integer grado;

    @NotNull(message = "El id de la institución no puede ser nulo")
    @Min(value = 1, message = "El id de la institución debe ser mayor que 0")
    private Long idInstitucion;
}