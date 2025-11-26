package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.asignacion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAsignacionService {
    ObjectResponse<Integer> contarSegunMision(Long idMision);
    ListResponse<AsignacionFilterAShortResponse> listarSegunMision(Long idMision);
    ListPageResponse<AsignacionFilterAShortResponse> listarPageSegunMision(Pageable pageable, Long idMision);
    ObjectResponse<AsignacionAllResponse> buscar(Long idAsignacion);
    ObjectResponse<AsignacionAllResponse> registrar(AsignacionCreateRequest request);
    ListResponse<AsignacionAllResponse> registrarAll(List<AsignacionCreateRequest> requests);
    ObjectResponse<AsignacionAllResponse> actualizar(Long idAsignacion, AsignacionUpdateRequest request);
    ObjectResponse<AsignacionEvidenciaResponse> actualizarEvidencia(Long idAsignacion, MultipartFile file);
    ObjectResponse<AsignacionEvidenciaResponse> buscarAsignacionEvidencia(Long idAsignacion);
    ObjectResponse<String> eliminarEvidencia(Long idAsignacion);
    ObjectResponse<String> eliminar(Long idAsignacion);
}
