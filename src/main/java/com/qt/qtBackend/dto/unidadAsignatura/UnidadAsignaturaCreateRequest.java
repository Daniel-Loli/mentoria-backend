package com.qt.qtBackend.dto.unidadAsignatura;

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
public class UnidadAsignaturaCreateRequest {

    @NotNull(message = "El id de la unidad no puede ser nulo")
    @Min(value = 1, message = "El id de la unidad debe ser mayor que 0")
    private Long idUnidad;

    @NotNull(message = "El id de la asignatura no puede ser nulo")
    @Min(value = 1, message = "El id de la asignatura debe ser mayor que 0")
    private Long idAsignatura;
}