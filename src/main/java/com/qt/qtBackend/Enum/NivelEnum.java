package com.qt.qtBackend.Enum;

public enum NivelEnum {

    INICIAL(1, "Inicial"),
    PRIMARIA(2, "Primaria"),
    SECUNDARIA(3, "Secundaria"),
    BASICA_ALTERNATIVA_INICIAL_INTERMEDIO(4, "Básica Alternativa - Inicial e Intermedio"),
    BASICA_ALTERNATIVA_AVANZADO(5, "Básica Alternativa - Avanzado"),
    BASICA_ESPECIAL(6, "Básica Especial"),
    BASICA_ESPECIAL_INICIAL(7, "Básica Especial - Inicial"),
    BASICA_ESPECIAL_PRIMARIA(8, "Básica Especial - Primaria"),
    TECNICO_PRODUCTIVA(9, "Técnico Productiva");

    private final Integer orden;
    private final String descripcion;

    NivelEnum(Integer orden, String descripcion) {
        this.orden = orden;
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
