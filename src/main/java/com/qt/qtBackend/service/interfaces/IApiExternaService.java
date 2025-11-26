package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.agente.ChatbotRequest;
import com.qt.qtBackend.dto.agente.ChatbotResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;

public interface IApiExternaService {
    ObjectResponse<ChatbotResponse> chatbot(ChatbotRequest request);
}
