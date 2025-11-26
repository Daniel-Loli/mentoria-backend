package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "mision")
public class Mision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_mision")
    private Long idMision;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMisionEnum estado;

    @Column(name = "fecha_inicio")
    private ZonedDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private ZonedDateTime fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_institucion",
            foreignKey = @ForeignKey(name = "FK_MISION_INSTITUCION"),nullable = false)
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
