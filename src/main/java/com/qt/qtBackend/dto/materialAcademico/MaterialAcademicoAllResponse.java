package com.qt.qtBackend.dto.materialAcademico;

import com.qt.qtBackend.Enum.TipoFile;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
import com.qt.qtBackend.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialAcademicoAllResponse {
    private Long idMaterialAcademico;
    private String descripcion;
    private TipoFile tipo;
    private String url;
    private UsuarioShortResponse usuario;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;
}
