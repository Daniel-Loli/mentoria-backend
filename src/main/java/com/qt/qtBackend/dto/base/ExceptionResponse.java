package com.qt.qtBackend.dto.base;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime datatime,
        String message,
        String path
) {

}
