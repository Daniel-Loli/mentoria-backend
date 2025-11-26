package com.qt.qtBackend.dto.asignacion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignacionCreateRequest {
    @NotNull(message = "El ID de equipo es obligatorio")
    private Long idMision;
    @NotNull(message = "El ID de alumnoInstitucion es obligatorio")
    private Long idAlumnoInstitucion;
}
