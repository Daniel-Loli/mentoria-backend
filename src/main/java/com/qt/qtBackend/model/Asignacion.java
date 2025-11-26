package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoAsignacionEnum;
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
@Table(name = "asignacion")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_mision",
            foreignKey = @ForeignKey(name = "FK_ASIGNACION_MISION"),nullable = false)
    private Mision mision;

    @ManyToOne
    @JoinColumn(name = "id_alumno_institucion",
            foreignKey = @ForeignKey(name = "FK_ASIGNACION_AI"),nullable = false)
    private AlumnoInstitucion alumnoInstitucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsignacionEnum estado;

    @Column(name = "puntaje")
    private Integer puntaje;

    @Column( name = "evidencia")
    private String evidencia;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "is_completado", nullable = false)
    private Boolean isCompletado;

    @Column(name="is_update", nullable = false)
    private Boolean isUpdate;

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
