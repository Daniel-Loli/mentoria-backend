package com.qt.qtBackend.dto.docenteInstitucion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocenteInstitucionCreateRequest {

    @NotNull(message = "El ID del docente es obligatorio")
    private Long idDocente;

    @NotNull(message = "El ID de la institución es obligatorio")
    private Long idInstitucion;

}