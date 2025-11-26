package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.unidad.NivelUnidadDTO;
import com.qt.qtBackend.dto.unidad.UnidadAllResponse;
import com.qt.qtBackend.dto.unidad.UnidadCreateRequest;
import com.qt.qtBackend.dto.unidad.UnidadShortResponse;

import java.util.List;

public interface IUnidadService {
    ObjectResponse<UnidadAllResponse> buscar(Long idUnidad);
    ListResponse<NivelUnidadDTO> listarNivelesGrados(Long idInstitucion);
    ObjectResponse<UnidadAllResponse> registrar(UnidadCreateRequest request);
    ListResponse<UnidadAllResponse> registrarAll(List<UnidadCreateRequest> requests);
    ObjectResponse<String> eliminar(Long idUnidad);
}
