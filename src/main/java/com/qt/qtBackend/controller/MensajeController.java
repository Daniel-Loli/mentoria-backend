package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.mensaje.MensajeCreateRequest;
import com.qt.qtBackend.dto.mensaje.MensajeShortResponse;
import com.qt.qtBackend.service.interfaces.IMensajeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mensaje")
@RequiredArgsConstructor
@Tag(name = "Mensaje", description = "Endpoints para la gestión de Mensaje")
public class MensajeController {

    private final IMensajeService service;

    // Listar mensajes por conversación sin paginar
    @GetMapping("/listar-segun-conversacion")
    public ListResponse<MensajeShortResponse> listar(
            @RequestParam("idConversacion") Long idConversacion
    ) {
        return service.listar(idConversacion);
    }

    // Listar mensajes con paginación
    @GetMapping("/listar-page-segun-conversacion")
    public ListPageResponse<MensajeShortResponse> listarPaginado(
            @RequestParam("idConversacion") Long idConversacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());


        return service.listarPaginado(pageable, idConversacion);
    }

    // Registrar mensaje en conversación
    @PostMapping("enviar-chatbot")
    public ListResponse<MensajeShortResponse> registrar(
            @Valid @RequestBody MensajeCreateRequest request
    ) {
        return service.registrar(request);
    }
}
