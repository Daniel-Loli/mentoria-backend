package com.qt.qtBackend.dto.agente;

import com.qt.qtBackend.Enum.TipoMensajeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialItemDto {
    private TipoMensajeEnum tipo;
    private String contenido;
}