package com.qt.qtBackend.Enum;

public enum TipoDocumentoEnum {

    DNI("Documento Nacional de Identidad", 8),
    CE("Carné de Extranjería", 12),
    PAS("Pasaporte", 12),
    PTP("Permiso Temporal de Permanencia", 15),
    RUC("Registro Único de Contribuyentes", 11);

    private final String descripcion;
    private final int longitudMaxima;

    TipoDocumentoEnum(String descripcion, int longitudMaxima) {
        this.descripcion = descripcion;
        this.longitudMaxima = longitudMaxima;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getLongitudMaxima() {
        return longitudMaxima;
    }
}