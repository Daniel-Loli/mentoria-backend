package com.qt.qtBackend.Enum;

public enum Message {

    CORREO_EN_USO(409, "El correo ya está en uso"),
    CORREO_NO_ENCONTRADO(404, "Correo no encontrado"),
    HORA_FIN_INICIO_ERROR(400, "La hora de fin debe ser posterior a la hora de inicio"),
    DURACION_DIFERENCIA_HORA_ERROR(400, "La duración no coincide con la diferencia entre hora inicio y fin"),
    CRUCE_DE_HORARIO_DISPONIBILIDAD(409, "El horario ingresado se cruza con otra disponibilidad existente en el mismo día"),
    LOGIN_ACCESS(200, "Inicio de sesión exitoso"),
    CREDENCIALES_INCORRECTAS(401, "Credenciales incorrectas"),
    CODIGO_EXISTENTE(409, "Este código ya existe"),
    ERROR_SERVIDOR(500, "Error interno del servidor"),
    RECURSOS_NO_ENCONTRADO(404, "Recurso no encontrado"),
    ERROR_VALIDACION(422, "Error de validación");

    private final int status;
    private final String texto;

    Message(int status, String texto) {
        this.status = status;
        this.texto = texto;
    }

    public int getStatus() {
        return status;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String toString() {
        return texto;
    }

    public String get() {
        return texto;
    }
}