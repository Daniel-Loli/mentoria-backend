package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.resultado.ResultadoAllResponse;
import com.qt.qtBackend.dto.resultado.ResultadoCreateRequest;
import com.qt.qtBackend.dto.resultado.ResultadoUpdateRequest;
import com.qt.qtBackend.service.interfaces.IResultadoService;
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
@RequestMapping("/resultado")
@RequiredArgsConstructor
@Tag(name = "Resultado", description = "Endpoints para la gestión de resultados de asignaturas")
public class ResultadoController {

    private final IResultadoService service;

    // -------------------------------- BUSCAR -------------------------------- //
    @Operation(summary = "Buscar resultado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado encontrado"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<ResultadoAllResponse>> buscar(
            @RequestParam("idResultado") Long idResultado
    ) {
        ObjectResponse<ResultadoAllResponse> response = service.buscar(idResultado);
        return ResponseEntity.status(response.status()).body(response);
    }

    // -------------------------------- CREAR -------------------------------- //
    @Operation(summary = "Registrar un resultado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resultado registrado"),
            @ApiResponse(responseCode = "404", description = "Matrícula o asignatura no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<ResultadoAllResponse>> registrar(
            @RequestBody ResultadoCreateRequest request
    ) {
        ObjectResponse<ResultadoAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples resultados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumen de registros procesados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<ResultadoAllResponse>> registrarAll(
            @RequestBody List<ResultadoCreateRequest> requests
    ) {
        ListResponse<ResultadoAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------ ACTUALIZAR ------------------------------ //
    @Operation(summary = "Actualizar un resultado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado actualizado"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idResultado}")
    public ResponseEntity<ObjectResponse<ResultadoAllResponse>> actualizar(
            @PathVariable Long idResultado,
            @RequestBody ResultadoUpdateRequest request
    ) {
        ObjectResponse<ResultadoAllResponse> response = service.actualizar(idResultado, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------- ELIMINAR ------------------------------- //
    @Operation(summary = "Eliminar resultado (borrado lógico)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado eliminado"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idResultado}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable Long idResultado
    ) {
        ObjectResponse<String> response = service.eliminar(idResultado);
        return ResponseEntity.status(response.status()).body(response);
    }
}
