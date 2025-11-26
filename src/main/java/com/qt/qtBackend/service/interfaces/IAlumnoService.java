package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.alumno.AlumnoAllResponse;
import com.qt.qtBackend.dto.alumno.AlumnoCreateRequest;
import com.qt.qtBackend.dto.alumno.AlumnoUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlumnoService {
    ObjectResponse<Integer> contar();
    ListResponse<AlumnoAllResponse> listar();
    ListPageResponse<AlumnoAllResponse> listarPage(Pageable pageable);
    ObjectResponse<AlumnoAllResponse> buscar(Long idAlumno);
    ObjectResponse<AlumnoAllResponse> registrar(AlumnoCreateRequest request);
    ListResponse<AlumnoAllResponse> registrarAll(List<AlumnoCreateRequest> requests);
    ObjectResponse<AlumnoAllResponse> actualizar(Long idAlumno, AlumnoUpdateRequest request);
    ObjectResponse<String> eliminar(Long idAlumno);
}
