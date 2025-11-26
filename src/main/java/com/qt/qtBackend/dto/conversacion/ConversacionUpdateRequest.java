package com.qt.qtBackend.dto.conversacion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversacionUpdateRequest {
    @NotNull(message = "El nombre es obligatorio")
    private String nombre;
}
