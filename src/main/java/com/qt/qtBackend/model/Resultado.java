package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.PeriodoEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "resultado")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_resultado")
    private Long idResultado;

    @ManyToOne
    @JoinColumn(name = "id_matricula",
            foreignKey = @ForeignKey(name = "FK_RESULTADO_MATRICULA"), nullable = false)
    private Matricula matricula;
    @ManyToOne
    @JoinColumn(name = "id_asignatura",
            foreignKey = @ForeignKey(name = "FK_RESULTADO_ASIGNATURA"), nullable = false)
    private Asignatura asignatura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodoEnum periodo;

    @Column(precision = 5, scale = 2)
    private BigDecimal nota;

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
