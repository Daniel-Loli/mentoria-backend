package com.qt.qtBackend.controller;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.mision.MisionAllResponse;
import com.qt.qtBackend.dto.mision.MisionCreateRequest;
import com.qt.qtBackend.dto.mision.MisionIAllResponse;
import com.qt.qtBackend.dto.mision.MisionUpdateRequest;
import com.qt.qtBackend.service.interfaces.IMisionService;
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
@RequestMapping("/mision")
@RequiredArgsConstructor
@Tag(name = "Misión", description = "Endpoints para la gestión de misiones")
public class MisionController {

    private final IMisionService service;
    @Operation(
            summary = "Recomendar misiones para un alumno",
            description = "Obtiene una lista de misiones recomendadas para un alumno específico según su institución."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de misiones recomendadas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron misiones recomendadas",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/recomendar-mision")
    public ResponseEntity<ListResponse<MisionIAllResponse>> recomendar(
            @RequestParam Long idAlumnoInstitucion
    ) {
        ListResponse<MisionIAllResponse> response = service.listarSegunAlumnoInstitucion(idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }


    // === CONTAR ===
    @Operation(summary = "Contar misiones habilitadas",
            description = "Obtiene la cantidad total de misiones registradas y habilitadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar-segun-institucion")
    public ResponseEntity<ObjectResponse<Integer>> contar(
            @RequestParam(required = false) EstadoMisionEnum estado,
            @RequestParam Long idInstitucion
    ) {
        ObjectResponse<Integer> response = service.contarSegunInstitucion(idInstitucion, estado);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === LISTAR ===
    @Operation(summary = "Listar misiones", description = "Obtiene una lista de todas las misiones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de misiones obtenida exitosamente")
    })
    @GetMapping("/listar-segun-institucion")
    public ResponseEntity<ListResponse<MisionIAllResponse>> listar(
            @RequestParam Long idInstitucion,
            @RequestParam(required = false) EstadoMisionEnum estado
    ) {
        ListResponse<MisionIAllResponse> response = service.listarSegunInstitucion(idInstitucion,estado);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === LISTAR PAGINADO ===
    @Operation(summary = "Listar misiones con paginación", description = "Obtiene una lista paginada y ordenada de las misiones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page-segun-institucion")
    public ResponseEntity<ListPageResponse<MisionIAllResponse>> listarPaginado(
            @RequestParam Long idInstitucion,
            @RequestParam(required = false) EstadoMisionEnum estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPageSegunInstitucion(pageable,idInstitucion,estado);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === BUSCAR POR ID ===
    @Operation(summary = "Buscar misión por ID", description = "Obtiene los detalles de una misión específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Misión encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<MisionAllResponse>> buscar(
            @RequestParam("idMision") Long idMision) {
        ObjectResponse<MisionAllResponse> response = service.buscar(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === REGISTRAR ===
    @Operation(summary = "Registrar misión", description = "Crea y registra una nueva misión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Misión registrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<MisionAllResponse>> registrar(
            @Valid @RequestBody MisionCreateRequest request) {
        ObjectResponse<MisionAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === REGISTRAR MÚLTIPLES ===
    @Operation(summary = "Registrar múltiples misiones",
            description = "Permite registrar varias misiones en una sola solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Misiones registradas exitosamente")
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<MisionAllResponse>> registrarAll(
            @Valid @RequestBody List<MisionCreateRequest> requests) {
        ListResponse<MisionAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === ACTUALIZAR ===
    @Operation(summary = "Actualizar misión", description = "Actualiza los datos de una misión existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Misión actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idMision}")
    public ResponseEntity<ObjectResponse<MisionAllResponse>> actualizar(
            @PathVariable Long idMision,
            @Valid @RequestBody MisionUpdateRequest request) {
        ObjectResponse<MisionAllResponse> response = service.actualizar(idMision, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // === ELIMINAR ===
    @Operation(summary = "Eliminar misión", description = "Realiza una eliminación lógica de la misión (la deshabilita).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Misión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idMision}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idMision) {
        ObjectResponse<String> response = service.eliminar(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }
}