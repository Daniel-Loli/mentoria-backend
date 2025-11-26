package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.*;
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
@Table(name = "alumno")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_alumno")
    private Long idAlumno;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ALUMNO_USUARIO")
    )
    private Usuario usuario;

    @Column(nullable = false, name = "nombres", length = 100)
    private String nombres;

    @Column(nullable = false, name = "apellidos", length = 100)
    private String apellidos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_documento")
    private TipoDocumentoEnum tipoDocumento;

    @Column(nullable = false, name = "doc_identidad")
    private String docIdentidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "sexo")
    private SexoEnum sexo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado")
    private EstadoAlumnoEnum estado;

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
