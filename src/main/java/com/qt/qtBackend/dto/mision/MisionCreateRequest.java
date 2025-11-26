package com.qt.qtBackend.dto.mision;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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
public class MisionCreateRequest {
    @NotBlank(message = "El titulo es obligatorios")
    private String titulo;
    private String descripcion;
    @NotBlank(message = "La idInstitución es obligatorios")
    private Long idInstitucion;
    private ZonedDateTime fechaInicio;
    private ZonedDateTime fechaFin;
}
