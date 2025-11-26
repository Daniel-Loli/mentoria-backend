package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaAllResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaCreateRequest;
import com.qt.qtBackend.dto.unidad.UnidadShortResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaShortResponse;

import java.util.List;

public interface IUnidadAsignaturaService {
    ObjectResponse<UnidadAsignaturaAllResponse> buscar(Long idUnidadAsignatura);
    ListResponse<UnidadAsignaturaShortResponse> listarPorUnidad(Long idUnidad);
    ObjectResponse<UnidadAsignaturaAllResponse> registrar(UnidadAsignaturaCreateRequest request);
    ListResponse<UnidadAsignaturaAllResponse> registrarAll(List<UnidadAsignaturaCreateRequest> requests);
    ObjectResponse<String> eliminar(Long idUnidadAsignatura);
}
