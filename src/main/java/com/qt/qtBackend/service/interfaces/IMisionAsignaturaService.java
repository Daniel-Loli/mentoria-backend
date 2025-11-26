package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.misionAsignatura.MAsignaturaShortResponse;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaCreateRequest;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaShortResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMisionAsignaturaService {
    ObjectResponse<Integer> contarSegunMision(Long idMision);
    ListResponse<MAsignaturaShortResponse> listarSegunMision(Long idMision);
    ListPageResponse<MAsignaturaShortResponse> listarPageSegunMision(Pageable pageable, Long idMision);
    ObjectResponse<MisionAsignaturaShortResponse> buscar(Long idMisionAsignatura);
    ObjectResponse<MisionAsignaturaShortResponse> registrar(MisionAsignaturaCreateRequest request);
    ListResponse<MisionAsignaturaShortResponse> registrarAll(List<MisionAsignaturaCreateRequest> requests);
    ObjectResponse<String> eliminar(Long idDocenteInstitucion);
}
