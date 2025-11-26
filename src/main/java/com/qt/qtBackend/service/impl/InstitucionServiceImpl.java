package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.EstadoUsuarioEnum;
import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.EstadoInstitucionEnum;
import com.qt.qtBackend.Enum.RolEnum;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.model.Rol;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.dto.institucion.*;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.repository.interfaces.IInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IRolRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IInstitucionService;
import com.qt.qtBackend.utils.GeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.qt.qtBackend.Enum.Message.CORREO_EN_USO;
import static com.qt.qtBackend.Enum.Modulo.ROL;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitucionServiceImpl
        extends CRUDImpl<Institucion, Long>
        implements IInstitucionService {

    private final IInstitucionRepo institucionRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IRolRepo rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final IMapperService mapperService;

    @Override
    protected IInstitucionRepo getRepo() {
        return institucionRepo;
    }

    public ObjectResponse<Integer> contar(){
        Integer contar = institucionRepo.contar();
        return new ObjectResponse<>(200, Modulo.INSTITUCION.contar(), contar);
    }

    @Override
    public ListResponse<InstitucionAllResponse> listar() {
        List<InstitucionAllResponse> lista = institucionRepo.listar()
                .stream()
                .map(mapperService::convInstitucionAll)
                .collect(Collectors.toList());
        return new ListResponse<>(200, Modulo.INSTITUCION.listado(), lista,lista.size());
    }


    @Override
    public ListPageResponse<InstitucionAllResponse> listarPaginado(Pageable pageable) {
        var page = institucionRepo.listarPage(pageable)
                .map(mapperService::convInstitucionAll);
        return new ListPageResponse<>(
                200,
                Modulo.INSTITUCION.listadoPage(),
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ObjectResponse<InstitucionAllResponse> buscar(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.INSTITUCION.encontrado(), mapperService.convInstitucionAll(institucionOpt.get()));
    }

    @Override
    public ObjectResponse<InstitucionAllResponse> registrar(InstitucionCreateRequest request) {
        // Validar email único
        if (usuarioRepo.buscarPorEmail(request.getEmail()).isPresent()) {
            return new ObjectResponse<>(409, CORREO_EN_USO.toString(), null);
        }
        RolEnum rol = RolEnum.INSTITUCION;
        Optional<Rol> rolOpt = rolRepo.buscarPorId(rol.getValor());
        if (rolOpt.isEmpty()) {
            return new ObjectResponse<>(404,ROL.noEncontrado(), null);
        }
        Rol rolUsuario = rolOpt.get();

        String codigo;
        do {
            codigo = "US"+ GeneratorUtil.generarCodigoNumerico(6);
        } while (usuarioRepo.existePorCodigo(codigo));


        // Crear usuario
        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .codigo(codigo)
                .telefono(request.getTelefono())
                .rol(rolUsuario)
                .estado(EstadoUsuarioEnum.ACTIVO)
                .build();
        usuarioRepo.save(usuario);

        // Crear institución
        Institucion institucion = Institucion.builder()
                .usuario(usuario)
                .nombre(request.getNombre())
                .numero(request.getNumero())
                .codigoLocal(request.getCodigoLocal())
                .niveles(request.getNiveles())
                .direccion(request.getDireccion())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .estado(EstadoInstitucionEnum.ACTIVA)
                .tipoPeriodo(request.getTipoPeriodo())
                .build();
        institucionRepo.save(institucion);

        return new ObjectResponse<>(201, Modulo.INSTITUCION.registrado(),mapperService.convInstitucionAll(institucion));
    }
    @Override
    public ListResponse<InstitucionAllResponse> registrarAll(List<InstitucionCreateRequest> requests) {
        List<InstitucionAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (InstitucionCreateRequest request : requests) {
            try {
                ObjectResponse<InstitucionAllResponse> response = registrar(request);
                if (response.status() == 201 && response.data() != null) {
                    registrados.add(response.data());
                } else {
                    errorCount++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                errorCount++;
            }
        }
        return new ListResponse<>(201,Modulo.INSTITUCION.resumenAllRegistro(registrados.size(), errorCount),registrados,registrados.size());
    }


    @Override
    public ObjectResponse<InstitucionAllResponse> actualizar(Long idInstitucion, InstitucionUpdateRequest request) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }

        Institucion institucion = institucionOpt.get();
        Usuario usuario = institucion.getUsuario();

        // Actualizar email si viene en el request
        if (request.getEmail() != null) {
            Optional<Usuario> existingUserOpt = usuarioRepo.buscarPorEmail(request.getEmail());
            if (existingUserOpt.isPresent() && !existingUserOpt.get().getIdUsuario().equals(usuario.getIdUsuario())) {
                return new ObjectResponse<>(409, CORREO_EN_USO.toString(), null);
            }
            usuario.setEmail(request.getEmail());
        }

        // Actualizar otros campos opcionales
        if (request.getTelefono() != null) usuario.setTelefono(request.getTelefono());
        usuarioRepo.save(usuario);

        if (request.getNombre() != null) institucion.setNombre(request.getNombre());
        if (request.getNumero() != null) institucion.setNumero(request.getNumero());
        if (request.getCodigoLocal() != null) institucion.setCodigoLocal(request.getCodigoLocal());
        if (request.getNiveles() != null) institucion.setNiveles(request.getNiveles());
        if (request.getDireccion() != null) institucion.setDireccion(request.getDireccion());
        if (request.getLatitud() != null) institucion.setLatitud(request.getLatitud());
        if (request.getLongitud() != null) institucion.setLongitud(request.getLongitud());
        if (request.getTipoPeriodo() != null) institucion.setTipoPeriodo(request.getTipoPeriodo());

        institucionRepo.save(institucion);

        return new ObjectResponse<>(200, Modulo.INSTITUCION.actualizado(), mapperService.convInstitucionAll(institucion));
    }

    @Override
    public ObjectResponse<String> eliminar(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }

        Institucion institucion = institucionOpt.get();
        institucion.setEnabled(false);
        institucionRepo.save(institucion);

        return new ObjectResponse<>(200, Modulo.INSTITUCION.eliminado(), null);
    }



}