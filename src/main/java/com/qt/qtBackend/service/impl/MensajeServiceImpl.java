package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.TipoMensajeEnum;
import com.qt.qtBackend.dto.agente.ChatbotRequest;
import com.qt.qtBackend.dto.agente.ChatbotResponse;
import com.qt.qtBackend.dto.agente.HistorialItemDto;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.mensaje.MensajeCreateRequest;
import com.qt.qtBackend.dto.mensaje.MensajeShortResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Conversacion;
import com.qt.qtBackend.model.Mensaje;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IConversacionRepo;
import com.qt.qtBackend.repository.interfaces.IMensajeRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IApiExternaService;
import com.qt.qtBackend.service.interfaces.IMensajeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MensajeServiceImpl
        extends CRUDImpl<Mensaje, Long>
        implements IMensajeService {

    private final IConversacionRepo conversacionRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IMensajeRepo mensajeRepo;
    private final IApiExternaService apiService;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<Mensaje, Long> getRepo() {
        return mensajeRepo;
    }

    //ORDENADO
    @Override
    public ListResponse<MensajeShortResponse> listar(Long idConversacion) {
        List<MensajeShortResponse> data = mensajeRepo.listarSegunConversacion(idConversacion)
                .stream()
                .sorted(Comparator.comparing(Mensaje::getCreatedAt).reversed())
                .map(mapperService::convMensajeShort)
                .toList();

        return new ListResponse<>(200, Modulo.MENSAJE.listado(), data, data.size());
    }

    @Override
    public ListPageResponse<MensajeShortResponse> listarPaginado(Pageable pageable, Long idConversacion) {
        Page<Mensaje> page = mensajeRepo.listarPageSegunConversacion(pageable,idConversacion);
        List<MensajeShortResponse> data = page.getContent().stream()
                .map(mapperService::convMensajeShort)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.MENSAJE.listadoPage(),
                data,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ListResponse<MensajeShortResponse> registrar(MensajeCreateRequest request) {
        Optional<Conversacion> conversacionOpt = conversacionRepo.buscarPorId(request.getIdConversacion());
        if (conversacionOpt.isEmpty()) {
            return new ListResponse<>(404, Modulo.CONVERSACION.noEncontrado(), null,null);
        }
        Conversacion conversacion = conversacionOpt.get();
        ListResponse<MensajeShortResponse> responseList = listar(conversacion.getIdConversacion());
        List<MensajeShortResponse> lista = responseList.data();
        List<HistorialItemDto> historialLista= lista
                .stream()
                .map(mapperService::convHistorialItem)
                .toList();

        ChatbotRequest chatRequest = ChatbotRequest.builder()
                .historial(historialLista)
                .pregunta(request.getContenido())
                .build();
        ObjectResponse<ChatbotResponse> chatbotResponse=apiService.chatbot(chatRequest);
        if(chatbotResponse.status()!=200){
            return new ListResponse<>(404, "Error al consultar al agente de chatbot", null,null);
        }

        Mensaje mensajeUsuario = Mensaje.builder()
                .conversacion(conversacion)
                .tipo(TipoMensajeEnum.USUARIO)
                .contenido(request.getContenido())
                .build();
        mensajeRepo.save(mensajeUsuario);
        //LOGICA CHAT BOT
        Mensaje mensajeChatbot = Mensaje.builder()
                .conversacion(conversacion)
                .tipo(TipoMensajeEnum.CHATBOT)
                .contenido(chatbotResponse.data().getRespuesta())
                .build();
        mensajeRepo.save(mensajeChatbot);
        List<MensajeShortResponse> data = List.of(
                mapperService.convMensajeShort(mensajeChatbot),
                mapperService.convMensajeShort(mensajeUsuario)
        );
        return new ListResponse<>(200,"Mensaje-respuesta registrado correctamente", data,data.size());

    }
}
