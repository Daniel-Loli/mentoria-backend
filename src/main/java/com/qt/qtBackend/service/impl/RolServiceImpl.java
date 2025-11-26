package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.RolEnum;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.rol.RolAllResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Rol;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IRolRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IRolService;
import com.qt.qtBackend.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolServiceImpl
        extends CRUDImpl<Rol, Long>
        implements IRolService {

    private final IRolRepo rolRepo;
    private final IMapperService mapperService;
    @Override
    protected IGenericRepo<Rol, Long> getRepo() {
        return null;
    }

    @Override
    public ListResponse<RolAllResponse> listar() {
        List<RolAllResponse> lista = rolRepo.listar()
                .stream()
                .map(mapperService::convRolAll)
                .toList();
        return new ListResponse<>(200, Modulo.ROL.listado(), lista,lista.size());
    }
    @Override
    public ObjectResponse<RolAllResponse> buscar(Long idRol) {
        return rolRepo.buscarPorId(idRol)
                .map(rol -> new ObjectResponse<>(200, Modulo.ROL.encontrado(), mapperService.convRolAll(rol)))
                .orElseGet(() -> new ObjectResponse<>(404, Modulo.ROL.noEncontrado(), null));
    }
}
