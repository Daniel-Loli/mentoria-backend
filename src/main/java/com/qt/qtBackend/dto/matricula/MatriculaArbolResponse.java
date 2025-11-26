package com.qt.qtBackend.dto.matricula;

import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.Enum.PeriodoEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculaArbolResponse {
    private Long idMatricula;
    private Integer anio;
    private NivelEnum nivel;
    private Integer grado;
    private EstadoMatriculaEnum estado;
    private TipoPeriodoEnum tipoPeriodo;
    private List<PeriodoDTO> periodos;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PeriodoDTO {
        private PeriodoEnum periodo;
        private List<AsignaturaNotaDTO> asignaturas;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AsignaturaNotaDTO {
        private Long idResultado;
        private String nombreAsignatura;
        private BigDecimal nota;
    }
}