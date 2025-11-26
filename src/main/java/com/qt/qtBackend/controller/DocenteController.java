package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docente.DocenteCreateRequest;
import com.qt.qtBackend.dto.docente.DocenteUpdateRequest;
import com.qt.qtBackend.service.interfaces.IDocenteService;
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
@RequestMapping("/docente")
@RequiredArgsConstructor
@Tag(name = "Docente", description = "Endpoints para la gestión de Docentes")
public class DocenteController {

    private final IDocenteService service;

    // --- CONTAR ---
    @Operation(
            summary = "Contar docentes habilitados",
            description = "Obtiene la cantidad total de docentes registrados y habilitados."
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
    @Operation(summary = "Listar docentes", description = "Obtiene una lista de todos los docentes registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de docentes obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<DocenteAllResponse>> listar() {
        ListResponse<DocenteAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR PAGINADO ---
    @Operation(summary = "Listar docentes con paginación", description = "Obtiene una lista paginada y ordenada de los docentes registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de docentes paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<DocenteAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPage(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- BUSCAR POR ID ---
    @Operation(summary = "Buscar docente por ID", description = "Obtiene los detalles de un docente específico usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<DocenteAllResponse>> buscar(
            @RequestParam("idDocente") Long idDocente) {
        ObjectResponse<DocenteAllResponse> response = service.buscar(idDocente);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR (Individual) ---
    @Operation(summary = "Registrar docente", description = "Crea y registra un nuevo docente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Docente registrado exitosamente"),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<DocenteAllResponse>> registrar(
            @Valid @RequestBody DocenteCreateRequest request) {
        ObjectResponse<DocenteAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR MÚLTIPLES ---
    @Operation(
            summary = "Registrar múltiples docentes",
            description = "Crea y registra múltiples docentes a partir de una lista de solicitudes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Docentes registrados exitosamente")
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<DocenteAllResponse>> registrarAll(
            @Valid @RequestBody List<DocenteCreateRequest> requests) {
        ListResponse<DocenteAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ACTUALIZAR ---
    @Operation(summary = "Actualizar docente", description = "Actualiza los datos de un docente existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idDocente}")
    public ResponseEntity<ObjectResponse<DocenteAllResponse>> actualizar(
            @PathVariable Long idDocente,
            @Valid @RequestBody DocenteUpdateRequest request) {
        ObjectResponse<DocenteAllResponse> response = service.actualizar(idDocente, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ELIMINAR ---
    @Operation(summary = "Eliminar docente", description = "Realiza la eliminación lógica de un docente (deshabilita su cuenta).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idDocente}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idDocente) {
        ObjectResponse<String> response = service.eliminar(idDocente);
        return ResponseEntity.status(response.status()).body(response);
    }
}
