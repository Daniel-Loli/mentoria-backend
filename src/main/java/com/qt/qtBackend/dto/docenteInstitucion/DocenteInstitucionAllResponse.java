package com.qt.qtBackend.dto.docenteInstitucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoDocenteInstitucion;
import com.qt.qtBackend.dto.docente.DocenteShortResponse;
import com.qt.qtBackend.dto.institucion.InstitucionShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocenteInstitucionAllResponse {
    private Long idDocenteInstitucion;
    private DocenteShortResponse docente;
    private InstitucionShortResponse institucion;
    private EstadoDocenteInstitucion estado;
    private ZonedDateTime createdAt;
    private ZonedDateTime updateAt;
    private Boolean enabled;
}
