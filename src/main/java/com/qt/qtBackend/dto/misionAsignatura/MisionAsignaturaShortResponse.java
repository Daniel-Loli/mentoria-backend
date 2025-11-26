package com.qt.qtBackend.dto.misionAsignatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.asignatura.AsignaturaShortResponse;
import com.qt.qtBackend.dto.mision.MisionShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MisionAsignaturaShortResponse {
    private Long idMisionAsignatura;
    private AsignaturaShortResponse asignatura;
    private MisionShortResponse mision;
}
