package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.TipoContainer;
import com.qt.qtBackend.Enum.TipoFile;
import com.qt.qtBackend.Enum.TipoMediaFileEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "material_academico")
public class MaterialAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_material_academico")
    private Long idMaterialAcademico;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoFile tipo;

    @Column(nullable = false, length = 500)
    private String url;

    @ManyToOne
    @JoinColumn( name = "id_usuario",
            foreignKey = @ForeignKey(name = "FK_MA_USUARIO"))
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false, name = "enabled")
    private Boolean enabled = true;
}
