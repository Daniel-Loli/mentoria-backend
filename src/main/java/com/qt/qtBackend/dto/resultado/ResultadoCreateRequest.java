package com.qt.qtBackend.dto.resultado;

import com.qt.qtBackend.Enum.PeriodoEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoCreateRequest {

    @NotNull(message = "El id de la matrícula no puede ser nulo")
    private Long idMatricula;

    @NotNull(message = "El id de la asignatura no puede ser nulo")
    private Long idAsignatura;

    @NotNull(message = "La nota no puede ser nula")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20")
    private BigDecimal nota;

    @NotNull(message = "El periodo no puede ser nulo")
    private PeriodoEnum periodo;
}