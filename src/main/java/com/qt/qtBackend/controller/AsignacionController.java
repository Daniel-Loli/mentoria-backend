package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.asignacion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.service.interfaces.IAsignacionService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/asignacion")
@RequiredArgsConstructor
@Tag(name = "Asignacion", description = "Endpoints para la gestión de asignaciones")
public class AsignacionController {

    private final IAsignacionService service;

    // -------------------------------- CONTAR -------------------------------- //

    @Operation(summary = "Contar asignaciones por mision")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar-segun-mision")
    public ResponseEntity<ObjectResponse<Integer>> contar(
            @RequestParam Long idMision
    ) {
        ObjectResponse<Integer> response = service.contarSegunMision(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    // -------------------------------- BUSCAR -------------------------------- //

    @Operation(summary = "Buscar asignación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<AsignacionAllResponse>> buscar(
            @RequestParam("idAsignacion") Long idAsignacion
    ) {
        ObjectResponse<AsignacionAllResponse> response = service.buscar(idAsignacion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------- CREAR -------------------------------- //

    @Operation(summary = "Registrar una asignación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "asignacin registrado"),
            @ApiResponse(responseCode = "404", description = "asignacion no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<AsignacionAllResponse>> registrar(
            @RequestBody AsignacionCreateRequest request
    ) {
        ObjectResponse<AsignacionAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples asignaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumen de registros procesados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<AsignacionAllResponse>> registrarAll(
            @RequestBody List<AsignacionCreateRequest> requests
    ) {
        ListResponse<AsignacionAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // -------------------------------- LISTAR -------------------------------- //

    @Operation(summary = "Listar asignaciones por equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "asignacion no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-segun-mision")
    public ResponseEntity<ListResponse<AsignacionFilterAShortResponse>> listarSegunEquipo(
            @RequestParam Long idMision
    ) {
        ListResponse<AsignacionFilterAShortResponse> response =
                service.listarSegunMision(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Listar asignaciones por equipo con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "asignacion no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-page-segun-mision")
    public ResponseEntity<ListPageResponse<AsignacionFilterAShortResponse>> listarPageSegunEquipo(
            @RequestParam Long idMision,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        ListPageResponse<AsignacionFilterAShortResponse> response = service.listarPageSegunMision(pageable, idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------ ACTUALIZAR ------------------------------ //

    @Operation(summary = "Actualizar una asignación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignacion actualizado"),
            @ApiResponse(responseCode = "404", description = "Asignacion no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idAsignacion}")
    public ResponseEntity<ObjectResponse<AsignacionAllResponse>> actualizar(
            @PathVariable Long idAsignacion,
            @RequestBody AsignacionUpdateRequest request
    ) {
        ObjectResponse<AsignacionAllResponse> response = service.actualizar(idAsignacion, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --------------------------- EVIDENCIA --------------------------- //
    @Operation(
            summary = "Actualizar evidencia de la asignación",
            description = "Sube una nueva evidencia y reemplaza la existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidencia actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o error en la subida",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("actualizar-evidencia/{idAsignacion}")
    public ResponseEntity<ObjectResponse<AsignacionEvidenciaResponse>> actualizarEvidencia(
            @PathVariable Long idAsignacion,
            @RequestParam("file") MultipartFile file
    ) {
        ObjectResponse<AsignacionEvidenciaResponse> response = service.actualizarEvidencia(idAsignacion, file);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Buscar evidencia de asignación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidencia encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación o evidencia no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar-evidencia")
    public ResponseEntity<ObjectResponse<AsignacionEvidenciaResponse>> buscarEvidencia(
            @RequestParam("idAsignacion") Long idAsignacion
    ) {
        ObjectResponse<AsignacionEvidenciaResponse> response = service.buscarAsignacionEvidencia(idAsignacion);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Eliminar evidencia de asignación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidencia eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asignación o evidencia no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar-evidencia/{idAsignacion}")
    public ResponseEntity<ObjectResponse<String>> eliminarEvidencia(
            @PathVariable Long idAsignacion
    ) {
        ObjectResponse<String> response = service.eliminarEvidencia(idAsignacion);
        return ResponseEntity.status(response.status()).body(response);
    }



    // ------------------------------- ELIMINAR ------------------------------- //

    @Operation(summary = "Eliminar asignación ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "asignacion eliminado"),
            @ApiResponse(responseCode = "404", description = "asignacion no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idAsignacion}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable Long idAsignacion
    ) {
        ObjectResponse<String> response = service.eliminar(idAsignacion);
        return ResponseEntity.status(response.status()).body(response);
    }

}
