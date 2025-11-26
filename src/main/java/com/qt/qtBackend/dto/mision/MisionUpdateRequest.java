package com.qt.qtBackend.dto.mision;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MisionUpdateRequest {

    private String titulo;

    private String descripcion;

    private EstadoMisionEnum estado;

    private ZonedDateTime fechaInicio;

    private ZonedDateTime fechaFin;

}