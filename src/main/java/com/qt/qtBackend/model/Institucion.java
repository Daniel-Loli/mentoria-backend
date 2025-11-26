package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoInstitucionEnum;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
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
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "institucion")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_institucion")
    private Long idInstitucion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_INSTITUCION_USUARIO")
    )
    private Usuario usuario;

    @Column(nullable = false, name = "nombre", length = 100)
    private String nombre;

    @Column(name = "numero", length = 4)
    private String numero;

    @Column(name = "codigo_local", length = 6)
    private String codigoLocal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado")
    private EstadoInstitucionEnum estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "institucion_niveles",
            joinColumns = @JoinColumn(
                    name = "id_institucion",
                    foreignKey = @ForeignKey(name = "FK_INSTITUCION_NIVELES")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private List<NivelEnum> niveles = new ArrayList<>();

    @Column(length = 250)
    private String direccion;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_periodo")
    private TipoPeriodoEnum tipoPeriodo;

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
