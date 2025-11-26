package com.qt.qtBackend.Enum;

public enum PeriodoEnum {
    PRIMER(1),
    SEGUNDO(2),
    TERCER(3),
    CUARTO(4),
    QUINTO(5),
    SEXTO(6),
    SEPTIMO(7),
    OCTAVO(8),
    NOVENO(9),
    DECIMO(10);

    private final Integer numero;

    PeriodoEnum(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumero() {
        return numero;
    }
}
