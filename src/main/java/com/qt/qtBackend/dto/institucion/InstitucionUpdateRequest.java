package com.qt.qtBackend.dto.institucion;

import com.qt.qtBackend.Enum.EstadoInstitucionEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstitucionUpdateRequest {
    @Email(message = "El email debe tener un formato válido")
    private String email;

    private String nombre;

    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos")
    private String telefono;

    @Pattern(regexp = "\\d{4}", message = "El numero debe tener exactamente 4 dígitos")
    private String numero;

    @Pattern(regexp = "\\d{6}", message = "El codigo local debe tener exactamente 6 dígitos")
    private String codigoLocal;


    private List<NivelEnum> niveles = new ArrayList<>();

    @Size(max = 250, message = "La dirección no puede superar los 250 caracteres")
    private String direccion;

    @DecimalMin(value = "-90.0", inclusive = true, message = "Latitud inválida")
    @DecimalMax(value = "90.0", inclusive = true, message = "Latitud inválida")
    private BigDecimal latitud;

    @DecimalMin(value = "-180.0", inclusive = true, message = "Longitud inválida")
    @DecimalMax(value = "180.0", inclusive = true, message = "Longitud inválida")
    private BigDecimal longitud;

    private TipoPeriodoEnum tipoPeriodo;


}