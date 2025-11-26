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
@Table(name = "conversacion")
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_conversacion")
    private Long idConversacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario",
            foreignKey = @ForeignKey(name = "FK_CONVERSACION_USUARIO"))
    private Usuario usuario;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

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
