package com.qt.qtBackend.dto.conversacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversacionShortResponse {
    private Integer idConversacion;
    private String nombre;
    private ZonedDateTime createdAt;
    private Boolean enabled;
}
