package com.qt.qtBackend.dto.matricula;

import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.model.AlumnoInstitucion;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class MatriculaCreateRequest {

    @NotNull(message = "El alumnoInstitucion es obligatorio")
    private Long idAlumnoInstitucion;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 2000, message = "El año debe ser mayor o igual a 2000") // ejemplo de año válido
    private Integer anio;

    @NotNull(message = "El nivel es obligatorio")
    private NivelEnum nivel;

    @NotNull(message = "El grado es obligatorio")
    @Min(value = 1, message = "El grado debe ser mayor o igual a 1")
    private Integer grado;

    @NotNull(message = "El estado de la matrícula es obligatorio")
    private EstadoMatriculaEnum estado;
}