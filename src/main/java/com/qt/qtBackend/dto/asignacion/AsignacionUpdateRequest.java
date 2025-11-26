package com.qt.qtBackend.dto.asignacion;

import com.qt.qtBackend.Enum.EstadoAsignacionEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignacionUpdateRequest {
    private EstadoAsignacionEnum estado;
    private Integer puntaje;
    private String respuesta;
}
