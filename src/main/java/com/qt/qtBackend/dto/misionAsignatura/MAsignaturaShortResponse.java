package com.qt.qtBackend.dto.misionAsignatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.asignatura.AsignaturaShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MAsignaturaShortResponse {
    private Long idMisionAsignatura;
    private AsignaturaShortResponse asignatura;
}
