package com.qt.qtBackend.dto.asignatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsignaturaShortResponse {
    private Long idAsignatura;
    private String nombre;
    private Boolean enabled;
}
