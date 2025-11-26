package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.rol.RolAllResponse;

import java.util.List;

public interface IRolService {
    ListResponse<RolAllResponse> listar();
    ObjectResponse<RolAllResponse> buscar(Long idRol);
}
