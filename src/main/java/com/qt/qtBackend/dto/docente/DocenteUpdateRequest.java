package com.qt.qtBackend.dto.docente;

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
public class DocenteUpdateRequest {
    @Email(message = "El formato del email no es válido")
    private String email;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 dígitos numéricos")
    private String telefono;

    @Size(max = 100, message = "Los nombres no deben exceder los 100 caracteres")
    private String nombres;

    @Size(max = 100, message = "Los apellidos no deben exceder los 100 caracteres")
    private String apellidos;

    private TipoDocumentoEnum tipoDocumento;

    @Size(min = 8, max = 12, message = "El documento debe tener entre 8 y 12 caracteres")
    private String docIdentidad;

    private SexoEnum sexo;
}
