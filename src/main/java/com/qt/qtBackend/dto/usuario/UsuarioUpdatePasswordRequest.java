package com.qt.qtBackend.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdatePasswordRequest {
    @NotBlank(message = "El password es un campo obligatorio")
    @Size(min = 6, max = 20, message = "El password debe tener entre 6 y 20 caracteres")
    private String password;

}
