package com.qt.qtBackend.service.impl;


import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl
        extends CRUDImpl<Usuario, Long>
        implements IUsuarioService {

    private final IMapperService mapperService;
    private final IUsuarioRepo usuarioRepo;

    @Override
    protected IGenericRepo<Usuario, Long> getRepo() {
        return usuarioRepo;
    }

    // === CONTAR ===
    @Override
    public ObjectResponse<Integer> contar() {
        Integer total = usuarioRepo.contar();
        return new ObjectResponse<>(200, Modulo.USUARIO.contar(), total);
    }

    // === LISTAR ===
    @Override
    public ListResponse<UsuarioAllResponse> listar() {
        List<UsuarioAllResponse> lista = usuarioRepo.listar()
                .stream()
                .sorted(Comparator.comparing(Usuario::getCreatedAt).reversed())
                .map(mapperService::convUsuarioAll)
                .toList();
        return new ListResponse<>(200, Modulo.USUARIO.listado(), lista,lista.size());
    }

    // === LISTAR PAGINADO ===
    @Override
    public ListPageResponse<UsuarioAllResponse> listarPage(Pageable pageable) {
        var page = usuarioRepo.listarPage(pageable)
                .map(mapperService::convUsuarioAll);
        return new ListPageResponse<>(
                200,
                Modulo.USUARIO.listadoPage(),
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()

        );
    }

    // === BUSCAR POR ID ===
    @Override
    public ObjectResponse<UsuarioAllResponse> buscar(Long idUsuario) {
        return usuarioRepo.buscarPorId(idUsuario)
                .map(usuario -> new ObjectResponse<>(200, Modulo.USUARIO.encontrado(), mapperService.convUsuarioAll(usuario)))
                .orElseGet(() -> new ObjectResponse<>(404, Modulo.USUARIO.noEncontrado(), null));
    }
}