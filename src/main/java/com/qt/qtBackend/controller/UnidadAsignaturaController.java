package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaAllResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaCreateRequest;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaShortResponse;
import com.qt.qtBackend.service.interfaces.IUnidadAsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidad-asignatura")
@RequiredArgsConstructor
@Tag(name = "Unidad Asignatura", description = "Endpoints para la gestión de Unidad Asignatura")
public class UnidadAsignaturaController {

    private final IUnidadAsignaturaService service;

    // ---------------- CRUD ---------------- //

    @Operation(summary = "Buscar Unidad Asignatura por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidad Asignatura encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidad Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<UnidadAsignaturaAllResponse>> buscar(@RequestParam Long idUnidadAsignatura) {
        ObjectResponse<UnidadAsignaturaAllResponse> response = service.buscar(idUnidadAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar Unidad Asignatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unidad Asignatura registrada"),
            @ApiResponse(responseCode = "404", description = "Unidad o Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<UnidadAsignaturaAllResponse>> registrar(@RequestBody UnidadAsignaturaCreateRequest request) {
        ObjectResponse<UnidadAsignaturaAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples Unidades Asignatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumen de registros procesados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<UnidadAsignaturaAllResponse>> registrarAll(@RequestBody List<UnidadAsignaturaCreateRequest> requests) {
        ListResponse<UnidadAsignaturaAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Eliminar Unidad Asignatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidad Asignatura eliminada"),
            @ApiResponse(responseCode = "404", description = "Unidad Asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @DeleteMapping("/eliminar/{idUnidadAsignatura}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idUnidadAsignatura) {
        ObjectResponse<String> response = service.eliminar(idUnidadAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ---------------- FILTROS / LISTADOS ---------------- //

    @Operation(summary = "Listar Unidades Asignatura por Unidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/listar-por-unidad")
    public ResponseEntity<ListResponse<UnidadAsignaturaShortResponse>> listarPorUnidad(@RequestParam Long idUnidad) {
        ListResponse<UnidadAsignaturaShortResponse> response = service.listarPorUnidad(idUnidad);
        return ResponseEntity.status(response.status()).body(response);
    }
}
