package com.qt.qtBackend.service.interfaces;


import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionAllResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionCreateRequest;
import com.qt.qtBackend.dto.conversacion.ConversacionShortResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface IConversacionService {
    ListResponse<ConversacionShortResponse> listarSegunUsuario(Long idUsuario);
    ListPageResponse<ConversacionShortResponse> listarPaginadoSegunUsuario(Pageable pageable,Long idUsuario);
    ObjectResponse<ConversacionAllResponse> buscar(Long idConversacion);
    ObjectResponse<ConversacionAllResponse> registrar(ConversacionCreateRequest request);
    ObjectResponse<ConversacionAllResponse> actualizar(Long idConversacion, ConversacionUpdateRequest request);
    ObjectResponse<String> eliminar(Long idConversacion);

}
