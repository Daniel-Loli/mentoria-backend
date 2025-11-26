package com.qt.qtBackend.dto.institucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.EstadoInstitucionEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
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
public class InstitucionAllResponse {
    private Long idInstitucion;
    private UsuarioShortResponse usuario;
    private String nombre;
    private String telefono;
    private String numero;
    private String codigoLocal;
    private EstadoInstitucionEnum estado;
    private TipoPeriodoEnum tipoPeriodo;
    private ZonedDateTime createdAt;
    private ZonedDateTime updateAt;
    private Boolean enabled;
}
