package com.qt.qtBackend.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageResponse(
        int status,
        String message
) {

}
