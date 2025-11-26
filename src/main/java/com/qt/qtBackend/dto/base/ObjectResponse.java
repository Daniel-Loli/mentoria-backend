package com.qt.qtBackend.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.Modulo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ObjectResponse<T>(
        int status,
        String message,
        T data
) {
}
