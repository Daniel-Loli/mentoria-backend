package com.qt.qtBackend.dto.resultado;

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
public class ResultadoUpdateRequest {
    @NotNull(message = "La nota no puede ser nula")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20")
    private BigDecimal nota;
}
