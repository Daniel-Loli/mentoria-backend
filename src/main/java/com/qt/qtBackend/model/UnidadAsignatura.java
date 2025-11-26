package com.qt.qtBackend.model;

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
@Table(name = "unidad_asignatura")
public class UnidadAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_unidad_asignatura")
    private Long idUnidadAsignatura;

    @ManyToOne
    @JoinColumn(name = "id_unidad",
            foreignKey = @ForeignKey(name = "FK_UNIDAD_uA"), nullable = false)
    private Unidad unidad;

    @ManyToOne
    @JoinColumn(name = "id_asignatura",
            foreignKey = @ForeignKey(name = "FK_ASIGNATURA_PA"), nullable = false)
    private Asignatura asignatura;

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
