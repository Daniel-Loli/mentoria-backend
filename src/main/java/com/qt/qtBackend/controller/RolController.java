package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.rol.RolAllResponse;
import com.qt.qtBackend.service.interfaces.IRolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rol")
@RequiredArgsConstructor
@Tag(name = "Rol", description = "Endpoints para la gestión de roles")
public class RolController {
    
    private final IRolService service;
    
    // === LISTAR ===
    @Operation(summary = "Listar Roles", description = "Obtiene una lista completa de los Roles registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<RolAllResponse>> listar() {
        ListResponse<RolAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }
    
    @Operation(summary = "Buscar Rol por ID", description = "Obtiene los datos specífico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<RolAllResponse>> buscar(
            @RequestParam("idRol") Long idRol) {
        ObjectResponse<RolAllResponse> response = service.buscar(idRol);
        return ResponseEntity.status(response.status()).body(response);
    }
    
}
