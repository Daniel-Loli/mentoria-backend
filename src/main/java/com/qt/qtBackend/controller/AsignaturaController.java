package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.asignatura.AsignaturaAllResponse;
import com.qt.qtBackend.dto.asignatura.AsignaturaCreateRequest;
import com.qt.qtBackend.dto.asignatura.AsignaturaUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.service.interfaces.IAsignaturaService;
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
@RequestMapping("/asignatura")
@RequiredArgsConstructor
@Tag(name = "Asignatura", description = "Endpoints para la gestión de Asignaturas")
public class AsignaturaController {

    private final IAsignaturaService service;

    // --- CONTAR ---
    @Operation(summary = "Contar asignaturas habilitadas", description = "Obtiene la cantidad total de asignaturas registradas y habilitadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR ---
    @Operation(summary = "Listar asignaturas", description = "Obtiene una lista de todas las asignaturas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaturas obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<AsignaturaAllResponse>> listar() {
        ListResponse<AsignaturaAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR PAGINADO ---
    @Operation(summary = "Listar asignaturas con paginación", description = "Obtiene una lista paginada y ordenada de las asignaturas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de asignaturas paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<AsignaturaAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPaginado(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- BUSCAR POR ID ---
    @Operation(summary = "Buscar asignatura por ID", description = "Obtiene los detalles de una asignatura específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignatura encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<AsignaturaAllResponse>> buscar(
            @RequestParam("idAsignatura") Long idAsignatura) {
        ObjectResponse<AsignaturaAllResponse> response = service.buscar(idAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR ---
    @Operation(summary = "Registrar asignatura", description = "Crea y registra una nueva asignatura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asignatura registrada exitosamente"),
            @ApiResponse(responseCode = "409", description = "Asignatura duplicada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<AsignaturaAllResponse>> registrar(
            @Valid @RequestBody AsignaturaCreateRequest request) {
        ObjectResponse<AsignaturaAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR VARIAS ---
    @Operation(summary = "Registrar múltiples asignaturas", description = "Crea y registra varias asignaturas en el sistema a partir de una lista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asignaturas registradas exitosamente")
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<AsignaturaAllResponse>> registrarAll(
            @Valid @RequestBody List<AsignaturaCreateRequest> request) {
        ListResponse<AsignaturaAllResponse> response = service.registrarAll(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ACTUALIZAR ---
    @Operation(summary = "Actualizar asignatura", description = "Actualiza los datos de una asignatura existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignatura actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "Asignatura duplicada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idAsignatura}")
    public ResponseEntity<ObjectResponse<AsignaturaAllResponse>> actualizar(
            @PathVariable Long idAsignatura,
            @Valid @RequestBody AsignaturaUpdateRequest request) {
        ObjectResponse<AsignaturaAllResponse> response = service.actualizar(idAsignatura, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ELIMINAR ---
    @Operation(summary = "Eliminar asignatura", description = "Elimina una asignatura usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignatura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idAsignatura}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idAsignatura) {
        ObjectResponse<String> response = service.eliminar(idAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }
}
