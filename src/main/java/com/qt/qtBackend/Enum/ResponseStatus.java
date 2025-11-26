package com.qt.qtBackend.Enum;

public enum ResponseStatus {
    // ===== 2xx Éxito =====
    OK(200, "La solicitud se procesó correctamente."),
    CREATED(201, "El recurso se creó correctamente."),
    ACCEPTED(202, "La solicitud fue aceptada y se está procesando."),
    NO_CONTENT(204, "La solicitud fue exitosa pero no hay contenido que devolver."),

    // ===== 3xx Redirección =====
    MOVED_PERMANENTLY(301, "El recurso se movió permanentemente a otra URL."),
    FOUND(302, "El recurso se movió temporalmente a otra URL."),
    NOT_MODIFIED(304, "El recurso no cambió, se puede usar la versión en caché."),

    // ===== 4xx Errores del cliente =====
    BAD_REQUEST(400, "La solicitud es inválida o mal formada."),
    UNAUTHORIZED(401, "No autenticado o credenciales incorrectas."),
    FORBIDDEN(403, "Autenticado, pero sin permisos para el recurso."),
    NOT_FOUND(404, "Recurso no encontrado."),
    METHOD_NOT_ALLOWED(405, "El método HTTP no está permitido en este endpoint."),
    CONFLICT(409, "Conflicto al procesar la solicitud."),
    UNPROCESSABLE_ENTITY(422, "Datos válidos sintácticamente, pero con errores de negocio."),

    // ===== 5xx Errores del servidor =====
    INTERNAL_SERVER_ERROR(500, "Error interno del servidor."),
    NOT_IMPLEMENTED(501, "El servidor no soporta la funcionalidad solicitada."),
    BAD_GATEWAY(502, "El servidor actuó como gateway y recibió una respuesta inválida."),
    SERVICE_UNAVAILABLE(503, "Servicio temporalmente no disponible."),
    GATEWAY_TIMEOUT(504, "El servidor no respondió a tiempo como gateway.");

    private final int status;
    private final String texto;

    // Constructor
    ResponseStatus(int codigo, String texto) {
        this.status = codigo;
        this.texto = texto;
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String gettexto() {
        return texto;
    }

    // Método para obtener el enum desde un código
    public static ResponseStatus fromCodigo(int codigo) {
        for (ResponseStatus estado : ResponseStatus.values()) {
            if (estado.getStatus() == codigo) {
                return estado;
            }
        }
        return null; // o lanzar excepción si prefieres
    }
}
