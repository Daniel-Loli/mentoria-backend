package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docente.DocenteCreateRequest;
import com.qt.qtBackend.dto.docente.DocenteUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDocenteService {
    ObjectResponse<Integer> contar();
    ListResponse<DocenteAllResponse> listar();
    ListPageResponse<DocenteAllResponse> listarPage(Pageable pageable);
    ObjectResponse<DocenteAllResponse> buscar(Long idDocente);
    ObjectResponse<DocenteAllResponse> registrar(DocenteCreateRequest request);
    ListResponse<DocenteAllResponse> registrarAll(List<DocenteCreateRequest> requests);
    ObjectResponse<DocenteAllResponse> actualizar(Long idDocente, DocenteUpdateRequest request);
    ObjectResponse<String> eliminar(Long idDocente);
}
