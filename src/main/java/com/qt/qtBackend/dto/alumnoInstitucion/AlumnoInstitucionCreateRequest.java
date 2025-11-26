package com.qt.qtBackend.dto.alumnoInstitucion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoInstitucionCreateRequest {

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long idAlumno;

    @NotNull(message = "El ID de la institución es obligatorio")
    private Long idInstitucion;

}
