package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {
    ObjectResponse<Integer> contar();
    ListResponse<UsuarioAllResponse> listar();
    ListPageResponse<UsuarioAllResponse> listarPage(Pageable pageable);
    ObjectResponse<UsuarioAllResponse> buscar(Long idUsuario);
}
