package com.qt.qtBackend.model;

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
@Table(name = "mision_asignatura")
public class MisionAsignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_mision_asignatura")
    private Long idMisionAsignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id_mision",
            foreignKey = @ForeignKey(name = "FK_MA_MISION"))
    private Mision mision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "id_asignatura",
            foreignKey = @ForeignKey(name = "FK_MA_ASIGNATURA"))
    private Asignatura asignatura;

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
