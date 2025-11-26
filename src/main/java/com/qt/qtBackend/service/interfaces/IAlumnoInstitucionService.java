package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.alumnoInstitucion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DInstitucionAllResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlumnoInstitucionService {
    ObjectResponse<Integer> contar();
    ObjectResponse<Integer> contarSegunAlumno(Long idAlumno);
    ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion);
    ListResponse<AlumnoInstitucionAllResponse> listar();
    ListResponse<AInstitucionAllResponse> listarSegunAlumno(Long idAlumno);
    ListResponse<AlumnoIAllResponse> listarSegunInstitucion(Long idInstitucion);
    ListPageResponse<AlumnoInstitucionAllResponse> listarPage(Pageable pageable);
    ListPageResponse<AInstitucionAllResponse> listarPageSegunAlumno(Pageable pageable, Long idAlumno);
    ListPageResponse<AlumnoIAllResponse> listarPageSegunInstitucion(Pageable pageable,Long idInstitucion);
    ObjectResponse<AlumnoInstitucionAllResponse> buscar(Long idAlumnoInstitucion);
    ObjectResponse<AlumnoInstitucionAllResponse> buscarSegunAlumnoInstitucion(Long idAlumno,Long idInstitucion);
    ObjectResponse<AlumnoInstitucionAllResponse> registrar(AlumnoInstitucionCreateRequest request);
    ListResponse<AlumnoInstitucionAllResponse> registrarAll(List<AlumnoInstitucionCreateRequest> requests);
    ObjectResponse<AlumnoInstitucionAllResponse> actualizar(Long idAlumnoInstitucion, AlumnoInstitucionUpdateRequest request);
    ObjectResponse<String> eliminar(Long idAlumnoInstitucion);
}
