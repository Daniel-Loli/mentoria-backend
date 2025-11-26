package com.qt.qtBackend.dto.misionAsignatura;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MisionAsignaturaCreateRequest {
    @NotNull(message = "El ID de la mision es obligatorio")
    private Long idMision;
    @NotNull(message = "El ID de la asignatura es obligatorio")
    private Long idAsignatura;
}
