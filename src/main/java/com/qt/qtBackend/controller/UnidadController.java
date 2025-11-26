package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.unidad.NivelUnidadDTO;
import com.qt.qtBackend.dto.unidad.UnidadAllResponse;
import com.qt.qtBackend.dto.unidad.UnidadCreateRequest;
import com.qt.qtBackend.service.interfaces.IUnidadService;
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
@RequestMapping("/unidad")
@RequiredArgsConstructor
@Tag(name = "Unidad", description = "Endpoints para la gestión de unidades")
public class UnidadController {

    private final IUnidadService service;

    // ---------------- CRUD ---------------- //

    @Operation(summary = "Buscar unidad por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<UnidadAllResponse>> buscar(@RequestParam Long idUnidad) {
        ObjectResponse<UnidadAllResponse> response = service.buscar(idUnidad);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar una unidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unidad registrada"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<UnidadAllResponse>> registrar(@RequestBody UnidadCreateRequest request) {
        ObjectResponse<UnidadAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples unidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumen de registros procesados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<UnidadAllResponse>> registrarAll(@RequestBody List<UnidadCreateRequest> requests) {
        ListResponse<UnidadAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Eliminar unidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidad eliminada"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @DeleteMapping("/eliminar/{idUnidad}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idUnidad) {
        ObjectResponse<String> response = service.eliminar(idUnidad);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ---------------- FILTROS / LISTADOS ---------------- //

    @Operation(summary = "Listar niveles y grados de una institución con sus unidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar-unidades-completa")
    public ResponseEntity<ListResponse<NivelUnidadDTO>> listarNivelesGrados(
            @RequestParam Long idInstitucion) {
        ListResponse<NivelUnidadDTO> response = service.listarNivelesGrados(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }
}
