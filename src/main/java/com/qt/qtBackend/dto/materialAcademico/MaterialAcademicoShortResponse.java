package com.qt.qtBackend.dto.materialAcademico;

import com.qt.qtBackend.Enum.TipoFile;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialAcademicoShortResponse {
    private Long idMaterialAcademico;
    private String descripcion;
    private TipoFile tipo;
    private String url;
    private Boolean enabled;
}
