package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.TipoMensajeEnum;
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
@Table(name = "mensaje")
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @ManyToOne
    @JoinColumn(name = "id_conversacion",
            foreignKey = @ForeignKey(name = "FK_MENSAJE_CONVERSACION"))
    private Conversacion conversacion;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "TIPO")
    private TipoMensajeEnum tipo;

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
