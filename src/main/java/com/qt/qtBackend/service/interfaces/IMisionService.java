package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.mision.MisionAllResponse;
import com.qt.qtBackend.dto.mision.MisionCreateRequest;
import com.qt.qtBackend.dto.mision.MisionIAllResponse;
import com.qt.qtBackend.dto.mision.MisionUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMisionService {
    ListResponse<MisionIAllResponse> listarSegunAlumnoInstitucion(Long idAlumnoInstitucion);
    ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion, EstadoMisionEnum estado);
    ListResponse<MisionIAllResponse> listarSegunInstitucion(Long idInstitucion, EstadoMisionEnum estado);
    ListPageResponse<MisionIAllResponse> listarPageSegunInstitucion(Pageable pageable,Long idInstitucion,EstadoMisionEnum estado);
    ObjectResponse<MisionAllResponse> buscar(Long idMision);
    ObjectResponse<MisionAllResponse> registrar(MisionCreateRequest request);
    ListResponse<MisionAllResponse> registrarAll(List<MisionCreateRequest> requests);
    ObjectResponse<MisionAllResponse> actualizar(Long idMision, MisionUpdateRequest request);
    ObjectResponse<String> eliminar(Long idMision);

}
