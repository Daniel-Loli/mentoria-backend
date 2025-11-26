package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.matricula.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMatriculaService {
    ObjectResponse<Integer> contarSegunAlumnoInstitucion(Long idAlumnoInstitucion);
    ListResponse<MatriculaShortResponse> listarSegunAlumnoInstitucion(Long idAlumnoInstitucion);
    ListPageResponse<MatriculaShortResponse> listarPageSegunAlumnoInstitucion(Pageable pageable, Long idAlumnoInstitucion);
    ObjectResponse<MatriculaAllResponse> buscar(Long idMatricula);
    ObjectResponse<MatriculaArbolResponse> obtenerMatriculaResultado(Long idMatricula);
    ObjectResponse<MatriculaAllResponse> registrar(MatriculaCreateRequest request);
    ListResponse<MatriculaAllResponse> registrarAll(List<MatriculaCreateRequest> requests);
    ObjectResponse<MatriculaAllResponse> actualizar(Long idMatricula, MatriculaUpdateRequest request);
    ObjectResponse<String> eliminar(Long idMatricula);
}
