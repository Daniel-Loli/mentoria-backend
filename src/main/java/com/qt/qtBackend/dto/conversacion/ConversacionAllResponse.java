package com.qt.qtBackend.dto.conversacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.dto.usuario.UsuarioShortResponse;
import lombok.*;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversacionAllResponse {

    private Integer idConversacion;
    private UsuarioShortResponse usuario;
    private String nombre;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Boolean enabled;

}
