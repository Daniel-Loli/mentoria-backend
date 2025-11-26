package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.dto.agente.ChatbotRequest;
import com.qt.qtBackend.dto.agente.ChatbotResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.service.interfaces.IApiExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiExternaServiceImpl
    implements IApiExternaService {

    private final WebClient webClientChatbot;

    @Override
    public ObjectResponse<ChatbotResponse> chatbot(ChatbotRequest request) {
        try {
            log.info("request: {}", request);

            ChatbotResponse response = webClientChatbot.post()
                    .uri("/agente_tutor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class).map(body ->
                                    new RuntimeException("Error del chatbot: " + body)
                            )
                    )
                    .bodyToMono(ChatbotResponse.class)
                    .block();

            log.info("response: {}", response);

            return new ObjectResponse<>(200, "Respuesta del chatbot obtenida correctamente", response);

        } catch (WebClientResponseException e) {
            return new ObjectResponse<>(e.getStatusCode().value(),
                    "El servicio del chatbot devolvió un error.",
                    null);

        } catch (WebClientRequestException e) {
            return new ObjectResponse<>(503,
                    "No se pudo conectar al servicio del chatbot. Intente más tarde.",
                    null);

        } catch (Exception e) {
            return new ObjectResponse<>(500,
                    "Error interno al procesar la solicitud del chatbot.",
                    null);
        }
    }

}