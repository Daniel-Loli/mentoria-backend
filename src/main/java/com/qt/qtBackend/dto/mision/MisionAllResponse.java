package com.qt.qtBackend.dto.mision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.dto.institucion.InstitucionShortResponse;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MisionAllResponse {
    private Long idMision;
    private String titulo;
    private String descripcion;
    private EstadoMisionEnum estado;
    private ZonedDateTime fechaInicio;
    private ZonedDateTime fechaFin;
    private InstitucionShortResponse institucion;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;
}
