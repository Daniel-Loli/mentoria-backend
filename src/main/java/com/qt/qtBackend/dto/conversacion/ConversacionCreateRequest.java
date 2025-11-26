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
public class ConversacionCreateRequest {

    @NotNull(message = "El idUsuario es obligatorio")
    private Long idUsuario;
    private String nombre;


}