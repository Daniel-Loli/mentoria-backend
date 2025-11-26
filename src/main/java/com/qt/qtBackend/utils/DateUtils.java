package com.qt.qtBackend.utils;

import java.time.LocalDate;

public class DateUtils {
    public static String anioActual() {
        return String.valueOf(LocalDate.now().getYear());
        // o también: Integer.toString(LocalDate.now().getYear());
    }
}
