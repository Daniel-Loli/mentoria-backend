package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.resultado.ResultadoAllResponse;
import com.qt.qtBackend.dto.resultado.ResultadoCreateRequest;
import com.qt.qtBackend.dto.resultado.ResultadoUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IResultadoService {
    ObjectResponse<ResultadoAllResponse> buscar(Long idResultado);
    ObjectResponse<ResultadoAllResponse> registrar(ResultadoCreateRequest request);
    ListResponse<ResultadoAllResponse> registrarAll(List<ResultadoCreateRequest> requests);
    ObjectResponse<ResultadoAllResponse> actualizar(Long idResultado, ResultadoUpdateRequest request);
    ObjectResponse<String> eliminar(Long idResultado);
}
