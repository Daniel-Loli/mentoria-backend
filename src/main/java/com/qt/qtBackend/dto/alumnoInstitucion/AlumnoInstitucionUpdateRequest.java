package com.qt.qtBackend.dto.alumnoInstitucion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoInstitucionUpdateRequest {
    private Long idAlumno;

    private Long idInstitucion;
}
