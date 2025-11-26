package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DInstitucionAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteIAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionCreateRequest;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionUpdateRequest;
import com.qt.qtBackend.service.interfaces.IDocenteInstitucionService;
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
@RequestMapping("/docente-institucion")
@RequiredArgsConstructor
@Tag(name = "Docente-Institución", description = "Endpoints para la gestión de relaciones entre Docentes e Instituciones")
public class DocenteInstitucionController {

    private final IDocenteInstitucionService service;

    // ====================== CONTAR ======================
    @Operation(summary = "Contar relaciones Docente-Institución", description = "Obtiene el número total de relaciones activas entre docentes e instituciones.")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== CONTAR SEGÚN DOCENTE ======================
    @Operation(
            summary = "Contar relaciones por Docente",
            description = "Obtiene el número total de instituciones asociadas a un docente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/contar-segun-docente")
    public ResponseEntity<ObjectResponse<Integer>> contarSegunDocente(
            @RequestParam("idDocente") Long idDocente
    ) {
        ObjectResponse<Integer> response = service.contarSegunDocente(idDocente);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== CONTAR SEGÚN INSTITUCIÓN ======================
    @Operation(
            summary = "Contar relaciones por Institución",
            description = "Obtiene el número total de docentes asociados a una institución específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada")
    })
    @GetMapping("/contar-segun-institucion")
    public ResponseEntity<ObjectResponse<Integer>> contarSegunInstitucion(
            @RequestParam("idInstitucion") Long idInstitucion
    ) {
        ObjectResponse<Integer> response = service.contarSegunInstitucion(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== LISTAR ======================
    @Operation(summary = "Listar todas las relaciones Docente-Institución", description = "Devuelve todas las relaciones registradas entre docentes e instituciones.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<DocenteInstitucionAllResponse>> listar() {
        ListResponse<DocenteInstitucionAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar solo docentes ---
    @Operation(summary = "Listar docentes asociados", description = "Obtiene la lista de docentes con sus instituciones asociadas.")
    @ApiResponse(responseCode = "200", description = "Lista de docentes obtenida exitosamente")
    @GetMapping("/listar-segun-institucion")
    public ResponseEntity<ListResponse<DocenteIAllResponse>> listarDocentes(
            @RequestParam("idInstitucion") Long idInstitucion
    ) {
        ListResponse<DocenteIAllResponse> response = service.listarSegunInstitucion(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar solo instituciones ---
    @Operation(summary = "Listar instituciones asociadas", description = "Obtiene la lista de instituciones con sus docentes asociados.")
    @ApiResponse(responseCode = "200", description = "Lista de instituciones obtenida exitosamente")
    @GetMapping("/listar-segun-docente")
    public ResponseEntity<ListResponse<DInstitucionAllResponse>> listarInstituciones(
            @RequestParam("idDocente") Long idDocente
    ) {
        ListResponse<DInstitucionAllResponse> response = service.listarSegunDocente(idDocente);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== LISTAR PAGINADO ======================
    @Operation(summary = "Listar relaciones con paginación", description = "Devuelve una lista paginada de las relaciones entre docentes e instituciones.")
    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente")
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<DocenteInstitucionAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        ListPageResponse<DocenteInstitucionAllResponse> response = service.listarPage(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar instituciones por docente ---
    @Operation(summary = "Listar instituciones de un docente", description = "Obtiene una lista de instituciones asociadas a un docente específico.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar-page-segun-docente")
    public ResponseEntity<ListPageResponse<DInstitucionAllResponse>> listarPageDocentes(
            @RequestParam("idDocente") Long idDocente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        ListPageResponse<DInstitucionAllResponse> response = service.listarPageSegunDocente(pageable, idDocente);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar docentes por institución ---
    @Operation(summary = "Listar docentes de una institución", description = "Obtiene una lista de docentes asociados a una institución específica.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar-page-segun-institucion")
    public ResponseEntity<ListPageResponse<DocenteIAllResponse>> listarPageInstituciones(
            @RequestParam("idInstitucion") Long idInstitucion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        ListPageResponse<DocenteIAllResponse> response = service.listarPageSegunInstitucion(pageable, idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== BUSCAR ======================
    @Operation(summary = "Buscar relación Docente-Institución por ID", description = "Obtiene los detalles de una relación específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<DocenteInstitucionAllResponse>> buscar(
            @RequestParam("idDocenteInstitucion") Long idDocenteInstitucion) {
        ObjectResponse<DocenteInstitucionAllResponse> response = service.buscar(idDocenteInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== REGISTRAR ======================
    @Operation(summary = "Registrar relación Docente-Institución", description = "Registra una nueva relación entre un docente y una institución.")
    @ApiResponse(responseCode = "201", description = "Relación registrada exitosamente")
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<DocenteInstitucionAllResponse>> registrar(
            @Valid @RequestBody DocenteInstitucionCreateRequest request) {
        ObjectResponse<DocenteInstitucionAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Registrar múltiples ---
    @Operation(summary = "Registrar múltiples relaciones Docente-Institución", description = "Permite registrar varias relaciones de docentes e instituciones al mismo tiempo.")
    @ApiResponse(responseCode = "201", description = "Relaciones registradas exitosamente")
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<DocenteInstitucionAllResponse>> registrarAll(
            @Valid @RequestBody List<DocenteInstitucionCreateRequest> requests) {
        ListResponse<DocenteInstitucionAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== ACTUALIZAR ======================
    @Operation(summary = "Actualizar relación Docente-Institución", description = "Actualiza una relación existente entre un docente y una institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idDocenteInstitucion}")
    public ResponseEntity<ObjectResponse<DocenteInstitucionAllResponse>> actualizar(
            @PathVariable Long idDocenteInstitucion,
            @Valid @RequestBody DocenteInstitucionUpdateRequest request) {
        ObjectResponse<DocenteInstitucionAllResponse> response = service.actualizar(idDocenteInstitucion, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== ELIMINAR ======================
    @Operation(summary = "Eliminar relación Docente-Institución", description = "Elimina (lógicamente) una relación entre un docente y una institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idDocenteInstitucion}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idDocenteInstitucion) {
        ObjectResponse<String> response = service.eliminar(idDocenteInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }
}
