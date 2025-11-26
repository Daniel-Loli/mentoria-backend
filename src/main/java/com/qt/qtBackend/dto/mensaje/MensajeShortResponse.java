package com.qt.qtBackend.dto.mensaje;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.TipoMensajeEnum;
import com.qt.qtBackend.model.Conversacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MensajeShortResponse {
    private Integer idMensaje;

    private String contenido;

    private TipoMensajeEnum tipo;

    private ZonedDateTime createdAt;

    private Boolean enabled;
}
