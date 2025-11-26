package com.qt.qtBackend.controller;


import com.qt.qtBackend.dto.alumnoInstitucion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.service.interfaces.IAlumnoInstitucionService;
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
@RequestMapping("/alumno-institucion")
@RequiredArgsConstructor
@Tag(name = "Alumno-Institución", description = "Endpoints para la gestión de relaciones entre Alumno e Instituciones")
public class AlumnoInstitucionController {
    private final IAlumnoInstitucionService service;

    // ====================== CONTAR ======================
    @Operation(summary = "Contar relaciones Alumno-Institución", description = "Obtiene el número total de relaciones activas entre Alumnos e instituciones.")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== CONTAR SEGÚN Alumno ======================
    @Operation(
            summary = "Contar relaciones por Alumno",
            description = "Obtiene el número total de instituciones asociadas a un Alumno específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado")
    })
    @GetMapping("/contar-segun-alumno")
    public ResponseEntity<ObjectResponse<Integer>> contarSegunAlumno(
            @RequestParam("idAlumno") Long idAlumno
    ) {
        ObjectResponse<Integer> response = service.contarSegunAlumno(idAlumno);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== CONTAR SEGÚN INSTITUCIÓN ======================
    @Operation(
            summary = "Contar relaciones por Institución",
            description = "Obtiene el número total de Alumnos asociados a una institución específica."
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
    @Operation(summary = "Listar todas las relaciones Alumno-Institución", description = "Devuelve todas las relaciones registradas entre Alumnos e instituciones.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<AlumnoInstitucionAllResponse>> listar() {
        ListResponse<AlumnoInstitucionAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar solo Alumnos ---
    @Operation(summary = "Listar Alumnos asociados", description = "Obtiene la lista de Alumnos con sus instituciones asociadas.")
    @ApiResponse(responseCode = "200", description = "Lista de Alumnos obtenida exitosamente")
    @GetMapping("/listar-segun-institucion")
    public ResponseEntity<ListResponse<AlumnoIAllResponse>> listarAlumnos(
            @RequestParam("idInstitucion") Long idInstitucion
    ) {
        ListResponse<AlumnoIAllResponse> response = service.listarSegunInstitucion(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar solo instituciones ---
    @Operation(summary = "Listar instituciones asociadas", description = "Obtiene la lista de instituciones con sus Alumnos asociados.")
    @ApiResponse(responseCode = "200", description = "Lista de instituciones obtenida exitosamente")
    @GetMapping("/listar-segun-alumno")
    public ResponseEntity<ListResponse<AInstitucionAllResponse>> listarInstituciones(
            @RequestParam("idAlumno") Long idAlumno
    ) {
        ListResponse<AInstitucionAllResponse> response = service.listarSegunAlumno(idAlumno);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== LISTAR PAGINADO ======================
    @Operation(summary = "Listar relaciones con paginación", description = "Devuelve una lista paginada de las relaciones entre Alumnos e instituciones.")
    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente")
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<AlumnoInstitucionAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        ListPageResponse<AlumnoInstitucionAllResponse> response = service.listarPage(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar instituciones por Alumno ---
    @Operation(summary = "Listar instituciones de un Alumno", description = "Obtiene una lista de instituciones asociadas a un Alumno específico.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar-page-segun-alumno")
    public ResponseEntity<ListPageResponse<AInstitucionAllResponse>> listarPageAlumnos(
            @RequestParam("idAlumno") Long idAlumno,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        ListPageResponse<AInstitucionAllResponse> response = service.listarPageSegunAlumno(pageable, idAlumno);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Listar Alumnos por institución ---
    @Operation(summary = "Listar Alumnos de una institución", description = "Obtiene una lista de Alumnos asociados a una institución específica.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    @GetMapping("/listar-page-segun-institucion")
    public ResponseEntity<ListPageResponse<AlumnoIAllResponse>> listarPageInstituciones(
            @RequestParam("idInstitucion") Long idInstitucion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        ListPageResponse<AlumnoIAllResponse> response = service.listarPageSegunInstitucion(pageable, idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== BUSCAR ======================
    @Operation(summary = "Buscar relación Alumno-Institución por ID", description = "Obtiene los detalles de una relación específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<AlumnoInstitucionAllResponse>> buscar(
            @RequestParam("idAlumnoInstitucion") Long idAlumnoInstitucion) {
        ObjectResponse<AlumnoInstitucionAllResponse> response = service.buscar(idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Buscar relación Alumno-Institución por ID", description = "Obtiene los detalles de una relación específica usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar-segun-alumno-institucion")
    public ResponseEntity<ObjectResponse<AlumnoInstitucionAllResponse>> buscarSegunAlumnoInstitucion(
            @RequestParam("idAlumno") Long idAlumno,
            @RequestParam("idInstitucion") Long idInstitucion
    ) {
        ObjectResponse<AlumnoInstitucionAllResponse> response = service.buscarSegunAlumnoInstitucion(idAlumno,idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }



    // ====================== REGISTRAR ======================
    @Operation(summary = "Registrar relación Alumno-Institución", description = "Registra una nueva relación entre un Alumno y una institución.")
    @ApiResponse(responseCode = "201", description = "Relación registrada exitosamente")
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<AlumnoInstitucionAllResponse>> registrar(
            @Valid @RequestBody AlumnoInstitucionCreateRequest request) {
        ObjectResponse<AlumnoInstitucionAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- Registrar múltiples ---
    @Operation(summary = "Registrar múltiples relaciones Alumno-Institución", description = "Permite registrar varias relaciones de Alumnos e instituciones al mismo tiempo.")
    @ApiResponse(responseCode = "201", description = "Relaciones registradas exitosamente")
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<AlumnoInstitucionAllResponse>> registrarAll(
            @Valid @RequestBody List<AlumnoInstitucionCreateRequest> requests) {
        ListResponse<AlumnoInstitucionAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== ACTUALIZAR ======================
    @Operation(summary = "Actualizar relación Alumno-Institución", description = "Actualiza una relación existente entre un Alumno y una institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idAlumnoInstitucion}")
    public ResponseEntity<ObjectResponse<AlumnoInstitucionAllResponse>> actualizar(
            @PathVariable Long idAlumnoInstitucion,
            @Valid @RequestBody AlumnoInstitucionUpdateRequest request) {
        ObjectResponse<AlumnoInstitucionAllResponse> response = service.actualizar(idAlumnoInstitucion, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ====================== ELIMINAR ======================
    @Operation(summary = "Eliminar relación Alumno-Institución", description = "Elimina (lógicamente) una relación entre un Alumno y una institución.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrada", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idAlumnoInstitucion}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idAlumnoInstitucion) {
        ObjectResponse<String> response = service.eliminar(idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }
}
