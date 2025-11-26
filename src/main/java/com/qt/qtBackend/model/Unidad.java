package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.NivelEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "unidad")
public class Unidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_unidad")
    private Long idUnidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private NivelEnum nivel;

    @Column(name = "grado", nullable = false)
    private Integer grado;

    @ManyToOne
    @JoinColumn(name = "id_institucion",
            foreignKey = @ForeignKey(name = "FK_UNIDAD_INSTITUCION"), nullable = false)
    private Institucion institucion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = true;
}
