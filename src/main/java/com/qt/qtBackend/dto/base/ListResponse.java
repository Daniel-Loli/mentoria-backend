package com.qt.qtBackend.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qt.qtBackend.Enum.Modulo;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListResponse<T>(
        int status,
        String message,
        List<T> data,
        Integer totalElements
) {
}
