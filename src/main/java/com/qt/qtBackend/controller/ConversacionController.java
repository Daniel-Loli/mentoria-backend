package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionAllResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionCreateRequest;
import com.qt.qtBackend.dto.conversacion.ConversacionShortResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionUpdateRequest;
import com.qt.qtBackend.service.interfaces.IConversacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversacion")
@RequiredArgsConstructor
@Tag(name = "Conversacion", description = "Endpoints para la gestión de Conversación")
public class ConversacionController {

    private final IConversacionService service;

    // === LISTAR ===
    @Operation(summary = "Listar conversaciones", description = "Obtiene una lista corta de conversaciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/listar-segun-usuario")
    public ResponseEntity<ListResponse<ConversacionShortResponse>> listar(
            @RequestParam("idUsuario") Long idUsuario
    ) {
        var response = service.listarSegunUsuario(idUsuario);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === LISTAR PAGINADO ===
    @Operation(summary = "Listar conversaciones paginadas", description = "Obtiene una lista paginada de conversaciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida correctamente")
    })
    @GetMapping("/listar-page-segun-usuario")
    public ResponseEntity<ListPageResponse<ConversacionShortResponse>> listarPaginado(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPaginadoSegunUsuario(pageable,idUsuario);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === BUSCAR POR ID ===
    @Operation(summary = "Buscar conversación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversación encontrada"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<ConversacionAllResponse>> buscar(
            @RequestParam("idConversacion") Long idConversacion) {
        var response = service.buscar(idConversacion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === REGISTRAR ===
    @Operation(summary = "Registrar conversación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conversación registrada")
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<ConversacionAllResponse>> registrar(
            @Valid @RequestBody ConversacionCreateRequest request) {
        var response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === ACTUALIZAR ===
    @Operation(summary = "Actualizar conversación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversación actualizada"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada")
    })
    @PutMapping("/actualizar/{idConversacion}")
    public ResponseEntity<ObjectResponse<ConversacionAllResponse>> actualizar(
            @PathVariable Long idConversacion,
            @Valid @RequestBody ConversacionUpdateRequest request) {
        var response = service.actualizar(idConversacion, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === ELIMINAR ===
    @Operation(summary = "Eliminar conversación", description = "Deshabilita una conversación por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversación eliminada"),
            @ApiResponse(responseCode = "404", description = "Conversación no encontrada")
    })
    @DeleteMapping("/eliminar/{idConversacion}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable Long idConversacion) {
        var response = service.eliminar(idConversacion);
        return ResponseEntity.status(response.status()).body(response);
    }
}
