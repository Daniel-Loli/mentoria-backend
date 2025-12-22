package com.qt.qtBackend.model;

import com.qt.qtBackend.Enum.EstadoUsuarioEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @EqualsAndHashCode.Include
    private Long idUsuario;

    @Column(nullable = false, name = "email")
    @Email
    private String email;

    @Column(nullable = false,name="password")
    private String password;

    @Column(nullable = false, name = "codigo",length = 8)
    private String codigo;

    @Column(nullable = true, name = "telefono", length = 9)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_rol",nullable = false,
            foreignKey = @ForeignKey(name = "FK_USUARIO_ROL"))
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado")
    private EstadoUsuarioEnum estado;

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
