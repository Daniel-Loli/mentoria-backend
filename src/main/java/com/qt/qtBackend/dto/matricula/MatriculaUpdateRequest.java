package com.qt.qtBackend.dto.matricula;

import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculaUpdateRequest {
    @NotNull(message = "El estado de la matrícula es obligatorio")
    private EstadoMatriculaEnum estado;
}
