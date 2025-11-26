package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionCreateRequest;
import com.qt.qtBackend.dto.mensaje.MensajeCreateRequest;
import com.qt.qtBackend.dto.mensaje.MensajeShortResponse;
import org.springframework.data.domain.Pageable;

public interface IMensajeService {
    ListResponse<MensajeShortResponse> listar(Long idConversacion);
    ListPageResponse<MensajeShortResponse> listarPaginado(Pageable pageable,Long idConversacion);
    ListResponse<MensajeShortResponse> registrar(MensajeCreateRequest request);
}
