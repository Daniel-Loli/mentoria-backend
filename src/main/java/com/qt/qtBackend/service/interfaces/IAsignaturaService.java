package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.asignatura.AsignaturaAllResponse;
import com.qt.qtBackend.dto.asignatura.AsignaturaCreateRequest;
import com.qt.qtBackend.dto.asignatura.AsignaturaUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAsignaturaService {
    ObjectResponse<Integer> contar();
    ListResponse<AsignaturaAllResponse> listar();
    ListPageResponse<AsignaturaAllResponse> listarPaginado(Pageable pageable);
    ObjectResponse<AsignaturaAllResponse> buscar(Long idAsignatura);
    ObjectResponse<AsignaturaAllResponse> registrar(AsignaturaCreateRequest request);
    ListResponse<AsignaturaAllResponse> registrarAll(List<AsignaturaCreateRequest> requests);
    ObjectResponse<AsignaturaAllResponse> actualizar(Long idAsignatura, AsignaturaUpdateRequest request);
    ObjectResponse<String> eliminar(Long idAsignatura);
}
