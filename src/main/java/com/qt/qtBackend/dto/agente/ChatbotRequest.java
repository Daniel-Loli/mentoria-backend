package com.qt.qtBackend.dto.agente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatbotRequest {
    private String pregunta;
    private List<HistorialItemDto> historial;
}