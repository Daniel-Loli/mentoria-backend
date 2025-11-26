package com.qt.qtBackend.dto.alumno;

import com.qt.qtBackend.Enum.SexoEnum;
import com.qt.qtBackend.Enum.TipoDocumentoEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoCreateRequest {

    @NotBlank(message = "El email es un campo obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El password es un campo obligatorio")
    @Size(min = 6, max = 20, message = "El password debe tener entre 6 y 20 caracteres")
    private String password;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no deben exceder los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no deben exceder los 100 caracteres")
    private String apellidos;

    @NotNull(message = "El tipo de documento es obligatorio")
    private TipoDocumentoEnum tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    private String docIdentidad;

    @NotNull(message = "El sexo es obligatorio")
    private SexoEnum sexo;
}
