package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionAllResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionCreateRequest;
import com.qt.qtBackend.dto.conversacion.ConversacionShortResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionUpdateRequest;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Conversacion;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IConversacionRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IConversacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.qt.qtBackend.utils.GeneratorUtil.generarCodigoNumerico;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversacionServiceImpl
        extends CRUDImpl<Conversacion, Long>
        implements IConversacionService {

    private final IConversacionRepo conversacionRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IMapperService mapperService;
    @Override
    protected IGenericRepo<Conversacion, Long> getRepo() {
        return conversacionRepo;
    }

    @Override
    public ListResponse<ConversacionShortResponse> listarSegunUsuario(Long idUsuario) {
        List<Conversacion> lista = conversacionRepo.listarSegunUsuario(idUsuario);
        List<ConversacionShortResponse> data = lista.stream()
                .sorted(Comparator.comparing(Conversacion::getCreatedAt).reversed())
                .map(mapperService::convConversacionShort)
                .toList();

        return new ListResponse<>(200, Modulo.CONVERSACION.listado(), data,data.size());
    }

    @Override
    public ListPageResponse<ConversacionShortResponse> listarPaginadoSegunUsuario(Pageable pageable,Long idUsuario) {
        Page<Conversacion> page = conversacionRepo.listarPageSegunUsuario(pageable,idUsuario);
        List<ConversacionShortResponse> data = page.getContent().stream()
                .map(mapperService::convConversacionShort)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.CONVERSACION.listadoPage(),
                data,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ObjectResponse<ConversacionAllResponse> buscar(Long idConversacion) {
        Optional<Conversacion> conversacionOpt = conversacionRepo.buscarPorId(idConversacion);
        if (conversacionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.CONVERSACION.noEncontrado(), null);
        }

        return new ObjectResponse<>(200, Modulo.CONVERSACION.encontrado(),
                mapperService.convConversacionAll(conversacionOpt.get()));
    }

    @Override
    public ObjectResponse<ConversacionAllResponse> registrar(ConversacionCreateRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorId(request.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.USUARIO.noEncontrado(), null);
        }
        Usuario usuario = usuarioOpt.get();

        String nombre="";
        if(request.getNombre() == null){
            nombre ="CONV"+generarCodigoNumerico(6);
        } else {
            nombre=request.getNombre();
        }

        Conversacion nuevo = Conversacion.builder()
                .usuario(usuario)
                .nombre(nombre)
                .build();
        conversacionRepo.save(nuevo);

        return new ObjectResponse<>(
                201,
                Modulo.CONVERSACION.registrado(),
                mapperService.convConversacionAll(nuevo)
        );
    }

    @Override
    public ObjectResponse<ConversacionAllResponse> actualizar(Long idConversacion, ConversacionUpdateRequest request) {
        Optional<Conversacion> conversacionOpt = conversacionRepo.buscarPorId(idConversacion);
        if (conversacionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.CONVERSACION.noEncontrado(), null);
        }
        Conversacion conversacion = conversacionOpt.get();
        conversacion.setNombre(request.getNombre());
        conversacionRepo.save(conversacion);

        return new ObjectResponse<>(
                201,
                Modulo.CONVERSACION.actualizado(),
                mapperService.convConversacionAll(conversacion)
        );
    }

    @Override
    public ObjectResponse<String> eliminar(Long idConversacion) {
        Optional<Conversacion> conversacionOpt = conversacionRepo.buscarPorId(idConversacion);
        if (conversacionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.CONVERSACION.noEncontrado(), null);
        }
        Conversacion conversacion = conversacionOpt.get();
        conversacion.setEnabled(false);
        conversacionRepo.save(conversacion);
        return new ObjectResponse<>(200, Modulo.CONVERSACION.eliminado(), null);

    }
}
