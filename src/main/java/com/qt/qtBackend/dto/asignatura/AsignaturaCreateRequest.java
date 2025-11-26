package com.qt.qtBackend.dto.asignatura;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignaturaCreateRequest {
    @NotBlank(message = "El nombre es un campo obligatorio")
    private String nombre;
}
