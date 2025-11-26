package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.misionAsignatura.MAsignaturaShortResponse;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaCreateRequest;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaShortResponse;
import com.qt.qtBackend.service.interfaces.IMisionAsignaturaService;
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
@RequestMapping("/mision-asignatura")
@RequiredArgsConstructor
@Tag(name = "mision-asignatura", description = "Endpoints para la gestión de relaciones entre misiones y asignaturas")
public class MisionAsignaturaController {

    private final IMisionAsignaturaService service;

    // ====================== CONTAR ======================
    @Operation(summary = "Contar asignaturas por misión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/contar-segun-mision")
    public ResponseEntity<ObjectResponse<Integer>> contarSegunMision(
            @RequestParam("idMision") Long idMision
    ) {
        ObjectResponse<Integer> response = service.contarSegunMision(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== LISTAR ======================
    @Operation(summary = "Listar asignaturas por misión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-segun-mision")
    public ResponseEntity<ListResponse<MAsignaturaShortResponse>> listarSegunMision(
            @RequestParam("idMision") Long idMision
    ) {
        var response = service.listarSegunMision(idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Listar asignaturas paginadas por misión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Misión no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-page-segun-mision")
    public ResponseEntity<ListPageResponse<MAsignaturaShortResponse>> listarPageSegunMision(
            @RequestParam("idMision") Long idMision,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var response = service.listarPageSegunMision(pageable, idMision);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== BUSCAR ======================
    @Operation(summary = "Buscar relación misión-asignatura por ID")
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<MisionAsignaturaShortResponse>> buscar(
            @RequestParam("idMisionAsignatura") Long idMisionAsignatura
    ) {
        var response = service.buscar(idMisionAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== REGISTRAR ======================
    @Operation(summary = "Registrar asignatura a una misión")
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<MisionAsignaturaShortResponse>> registrar(
            @Valid @RequestBody MisionAsignaturaCreateRequest request
    ) {
        var response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples asignaciones de misión-asignatura")
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<MisionAsignaturaShortResponse>> registrarAll(
            @Valid @RequestBody List<MisionAsignaturaCreateRequest> requests
    ) {
        var response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== ELIMINAR ======================
    @Operation(summary = "Eliminar relación misión-asignatura")
    @DeleteMapping("/eliminar/{idMisionAsignatura}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable Long idMisionAsignatura
    ) {
        var response = service.eliminar(idMisionAsignatura);
        return ResponseEntity.status(response.status()).body(response);
    }
}
