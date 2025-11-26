package com.qt.qtBackend.dto.mensaje;

import com.qt.qtBackend.Enum.TipoMensajeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MensajeCreateRequest {

    @NotNull(message = "El id de la conversación es obligatorio")
    private Long idConversacion;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 1000, message = "El contenido no puede superar los 1000 caracteres")
    private String contenido;

}