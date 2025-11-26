package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import com.qt.qtBackend.service.interfaces.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Endpoints para la gestión de Usuarios")
public class UsuarioController {

    private final IUsuarioService service;

    // --- CONTAR ---
    @Operation(summary = "Contar usuarios habilitados", description = "Obtiene la cantidad total de usuarios registrados y habilitados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR ---
    @Operation(summary = "Listar usuarios", description = "Obtiene una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<UsuarioAllResponse>> listar() {
        ListResponse<UsuarioAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR PAGINADO ---
    @Operation(summary = "Listar usuarios con paginación", description = "Obtiene una lista paginada y ordenada de los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<UsuarioAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPage(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- BUSCAR POR ID ---
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los detalles de un usuario específico usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<UsuarioAllResponse>> buscar(
            @RequestParam("idUsuario") Long idUsuario) {
        ObjectResponse<UsuarioAllResponse> response = service.buscar(idUsuario);
        return ResponseEntity.status(response.status()).body(response);
    }
}
