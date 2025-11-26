package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docente.DocenteCreateRequest;
import com.qt.qtBackend.dto.docente.DocenteUpdateRequest;
import com.qt.qtBackend.dto.docenteInstitucion.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDocenteInstitucionService {
    ObjectResponse<Integer> contar();
    ObjectResponse<Integer> contarSegunDocente(Long idDocente);
    ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion);
    ListResponse<DocenteInstitucionAllResponse> listar();
    ListResponse<DInstitucionAllResponse> listarSegunDocente(Long idDocente);
    ListResponse<DocenteIAllResponse> listarSegunInstitucion(Long idInstitucion);
    ListPageResponse<DocenteInstitucionAllResponse> listarPage(Pageable pageable);
    ListPageResponse<DInstitucionAllResponse> listarPageSegunDocente(Pageable pageable,Long idDocente);
    ListPageResponse<DocenteIAllResponse> listarPageSegunInstitucion(Pageable pageable,Long idInstitucion);
    ObjectResponse<DocenteInstitucionAllResponse> buscar(Long idDocenteInstitucion);
    ObjectResponse<DocenteInstitucionAllResponse> registrar(DocenteInstitucionCreateRequest request);
    ListResponse<DocenteInstitucionAllResponse> registrarAll(List<DocenteInstitucionCreateRequest> requests);
    ObjectResponse<DocenteInstitucionAllResponse> actualizar(Long idDocenteInstitucion, DocenteInstitucionUpdateRequest request);
    ObjectResponse<String> eliminar(Long idDocenteInstitucion);
}
