package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoMatriculaEnum;
import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
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
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_matricula")
    private Long idMatricula;

    @ManyToOne
    @JoinColumn(name = "id_alumno_institucion",
            foreignKey = @ForeignKey(name = "FK_MATRICULA_AI"), nullable = false)
    private AlumnoInstitucion alumnoInstitucion;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private NivelEnum nivel;

    @Column(name = "grado", nullable = false)
    private Integer grado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoMatriculaEnum estado;

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