package com.qt.qtBackend.dto.resultado;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.PeriodoEnum;
import com.qt.qtBackend.dto.asignatura.AsignaturaShortResponse;
import com.qt.qtBackend.dto.matricula.MatriculaShortResponse;
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
public class ResultadoAllResponse {
    private Long idResultado;
    private MatriculaShortResponse matricula;
    private AsignaturaShortResponse asignatura;
    private BigDecimal nota;
    private PeriodoEnum periodo;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;
}
