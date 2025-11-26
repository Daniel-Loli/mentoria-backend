package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.institucion.*;
import com.qt.qtBackend.service.interfaces.IInstitucionService;
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

import java.util.List;

@RestController
@RequestMapping("/institucion")
@RequiredArgsConstructor
@Tag(name = "Institución", description = "Endpoints para la gestión de Instituciones")
public class InstitucionController {

    private final IInstitucionService service;


    // --- CONTAR ---
    @Operation(
            summary = "Contar instituciones habilitadas",
            description = "Obtiene la cantidad total de instituciones registradas y habilitadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }


    // --- LISTAR ---
    @Operation(summary = "Listar instituciones", description = "Obtiene una lista de todas las instituciones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de instituciones obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<InstitucionAllResponse>> listar() {
        ListResponse<InstitucionAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR PAGINADO ---
    @Operation(summary = "Listar instituciones con paginación", description = "Obtiene una lista paginada y ordenada de las instituciones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista instituciones paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<InstitucionAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());


        var response = service.listarPaginado(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }


    // --- BUSCAR POR ID ---
    @Operation(summary = "Buscar institución por ID", description = "Obtiene los detalles de una institución específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<InstitucionAllResponse>> buscar(
            @RequestParam("idInstitucion") Long idInstitucion) {

        ObjectResponse<InstitucionAllResponse> response = service.buscar(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR (Individual) ---
    @Operation(summary = "Registrar institución", description = "Crea y registra una nueva institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institución registrada exitosamente"),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<InstitucionAllResponse>> registrar(
            @Valid @RequestBody InstitucionCreateRequest request) {
        ObjectResponse<InstitucionAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }
    @Operation(
            summary = "Registrar múltiples instituciones",
            description = "Crea y registra múltiples instituciones en el sistema a partir de una lista."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instituciones registradas exitosamente")
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<InstitucionAllResponse>> registrarAll(
            @Valid @RequestBody List<InstitucionCreateRequest> request) {
        ListResponse<InstitucionAllResponse> response = service.registrarAll(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ACTUALIZAR ---
    @Operation(summary = "Actualizar institución", description = "Actualiza los datos de una institución existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idInstitucion}")
    public ResponseEntity<ObjectResponse<InstitucionAllResponse>> actualizar(
            @PathVariable Long idInstitucion,
            @Valid @RequestBody InstitucionUpdateRequest request) {
        ObjectResponse<InstitucionAllResponse> response = service.actualizar(idInstitucion, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ELIMINAR ---
    @Operation(summary = "Eliminar institución", description = "Elimina una institución usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institución eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idInstitucion}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idInstitucion) {
        ObjectResponse<String> response = service.eliminar(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }
}