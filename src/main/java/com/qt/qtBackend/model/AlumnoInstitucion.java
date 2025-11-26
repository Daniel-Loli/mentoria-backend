package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoAlumnoInstitucion;
import com.qt.qtBackend.Enum.EstadoDocenteInstitucion;
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
@Table(name = "alumno_institucion")
public class AlumnoInstitucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_alumno_institucion")
    private Long idAlumnoInstitucion;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_docente",
            foreignKey = @ForeignKey(name = "FK_AI_DOCENTE"))
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_institucion",
            foreignKey = @ForeignKey(name = "FK_AI_INSTITUCION"))
    private Institucion institucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado")
    private EstadoAlumnoInstitucion estado;

    @Column(name = "puntaje_total")
    private Integer puntajeTotal;


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
