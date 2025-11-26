package com.qt.qtBackend.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListPageResponse<T>(
        int status,
        String message,
        List<T> data,
        int page,            // Página actual
        int size,            // Tamaño de página
        long totalElements,  // Total de registros
        int totalPages,      // Total de páginas
        boolean last         // Indica si es la última página
) {}
