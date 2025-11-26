package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.matricula.*;
import com.qt.qtBackend.service.interfaces.IMatriculaService;
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

import java.util.List;

@RestController
@RequestMapping("/matricula")
@RequiredArgsConstructor
@Tag(name = "Matricula", description = "Endpoints para la gestión de matrícula")
public class MatriculaController {

    private final IMatriculaService service;

    // -------------------------------- CONTAR -------------------------------- //
    @Operation(summary = "Contar matrículas por alumno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar-segun-alumnoinstitucion")
    public ResponseEntity<ObjectResponse<Integer>> contar(
            @RequestParam Long idAlumnoInstitucion
    ) {
        ObjectResponse<Integer> response = service.contarSegunAlumnoInstitucion(idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // -------------------------------- BUSCAR -------------------------------- //
    @Operation(summary = "Buscar matrícula por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matrícula encontrada"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<MatriculaAllResponse>> buscar(
            @RequestParam("idMatricula") Long idMatricula
    ) {
        ObjectResponse<MatriculaAllResponse> response = service.buscar(idMatricula);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/buscar-matricula-completa")
    public ResponseEntity<ObjectResponse<MatriculaArbolResponse>> obtenerMatriculaCompleta(
            @RequestParam("idMatricula") Long idMatricula
    ) {
        ObjectResponse<MatriculaArbolResponse> response = service.obtenerMatriculaResultado(idMatricula);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------- CREAR -------------------------------- //
    @Operation(summary = "Registrar una matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matrícula registrada"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<MatriculaAllResponse>> registrar(
            @RequestBody MatriculaCreateRequest request
    ) {
        ObjectResponse<MatriculaAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar múltiples matrículas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resumen de registros procesados"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<MatriculaAllResponse>> registrarAll(
            @RequestBody List<MatriculaCreateRequest> requests
    ) {
        ListResponse<MatriculaAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // -------------------------------- LISTAR -------------------------------- //
    @Operation(summary = "Listar matrículas por alumnoinstitucion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-segun-alumnoinstitucion")
    public ResponseEntity<ListResponse<MatriculaShortResponse>> listarSegunAlumno(
            @RequestParam Long idAlumnoInstitucion
    ) {
        ListResponse<MatriculaShortResponse> response = service.listarSegunAlumnoInstitucion(idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Listar matrículas por alumnoinstitucion con paginación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista paginada obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/listar-page-segun-alumnoinstitucion")
    public ResponseEntity<ListPageResponse<MatriculaShortResponse>> listarPageSegunAlumno(
            @RequestParam Long idAlumnoInstitucion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        ListPageResponse<MatriculaShortResponse> response = service.listarPageSegunAlumnoInstitucion(pageable, idAlumnoInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------ ACTUALIZAR ------------------------------ //
    @Operation(summary = "Actualizar una matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matrícula actualizada"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idMatricula}")
    public ResponseEntity<ObjectResponse<MatriculaAllResponse>> actualizar(
            @PathVariable Long idMatricula,
            @RequestBody MatriculaUpdateRequest request
    ) {
        ObjectResponse<MatriculaAllResponse> response = service.actualizar(idMatricula, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------------- ELIMINAR ------------------------------- //
    @Operation(summary = "Eliminar matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matrícula eliminada"),
            @ApiResponse(responseCode = "404", description = "Matrícula no encontrada",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idMatricula}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable Long idMatricula
    ) {
        ObjectResponse<String> response = service.eliminar(idMatricula);
        return ResponseEntity.status(response.status()).body(response);
    }

}
