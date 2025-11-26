package com.qt.qtBackend.Enum;

public enum RolEnum {
    UGEL(1L),
    INSTITUCION(2L),
    DOCENTE(3L),
    ALUMNO(4L),
    AGENTE(5L);

    private final Long valor;

    RolEnum(Long valor) {
        this.valor = valor;
    }

    // Getter
    public Long getValor() {
        return valor;
    }

    public static RolEnum fromValor(Long valor) {
        for (RolEnum rol : RolEnum.values()) {
            if (rol.getValor().equals(valor)) {
                return rol;
            }
        }
        return null;
    }
}
