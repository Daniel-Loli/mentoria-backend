package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.institucion.InstitucionAllResponse;
import com.qt.qtBackend.dto.institucion.InstitucionCreateRequest;
import com.qt.qtBackend.dto.institucion.InstitucionUpdateRequest;
import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.service.base.ICRUD; // Asumo que tienes un ICRUD base
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInstitucionService extends ICRUD<Institucion, Long> {

    ListResponse<InstitucionAllResponse> listar();
    ObjectResponse<Integer> contar();
    ListPageResponse<InstitucionAllResponse> listarPaginado(Pageable pageable);
    ObjectResponse<InstitucionAllResponse> buscar(Long idInstitucion);
    ObjectResponse<InstitucionAllResponse> registrar(InstitucionCreateRequest request);
    ListResponse<InstitucionAllResponse> registrarAll(List<InstitucionCreateRequest> requests);
    ObjectResponse<InstitucionAllResponse> actualizar(Long idInstitucion, InstitucionUpdateRequest request);
    ObjectResponse<String> eliminar(Long idInstitucion);
}