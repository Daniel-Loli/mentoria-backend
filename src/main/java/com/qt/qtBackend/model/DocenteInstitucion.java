package com.qt.qtBackend.model;

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
@Table(name = "docente_institucion")
public class DocenteInstitucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_docente_institucion")
    private Long idDocenteInstitucion;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_docente",
            foreignKey = @ForeignKey(name = "FK_DI_DOCENTE"))
    private Docente docente;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_institucion",
            foreignKey = @ForeignKey(name = "FK_DI_INSTITUCION"))
    private Institucion institucion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado")
    private EstadoDocenteInstitucion estado;

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
