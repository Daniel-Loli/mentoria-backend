package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.alumno.AlumnoAllResponse;
import com.qt.qtBackend.dto.alumno.AlumnoCreateRequest;
import com.qt.qtBackend.dto.alumno.AlumnoUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.service.interfaces.IAlumnoService;
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
@RequestMapping("/alumno")
@RequiredArgsConstructor
@Tag(name = "Alumno", description = "Endpoints para la gestión de alumnos")
public class AlumnoController {

    private final IAlumnoService service;

    // --- CONTAR ---
    @Operation(
            summary = "Contar Alumnos habilitados",
            description = "Obtiene la cantidad total de Alumnos registrados y habilitados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    })
    @GetMapping("/contar")
    public ResponseEntity<ObjectResponse<Integer>> contar() {
        ObjectResponse<Integer> response = service.contar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR ---
    @Operation(summary = "Listar Alumnos", description = "Obtiene una lista de todos los Alumnos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Alumnos obtenida exitosamente")
    })
    @GetMapping("/listar")
    public ResponseEntity<ListResponse<AlumnoAllResponse>> listar() {
        ListResponse<AlumnoAllResponse> response = service.listar();
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- LISTAR PAGINADO ---
    @Operation(summary = "Listar Alumnos con paginación", description = "Obtiene una lista paginada y ordenada de los Alumnos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Alumnos paginada obtenida exitosamente")
    })
    @GetMapping("/listar-page")
    public ResponseEntity<ListPageResponse<AlumnoAllResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var response = service.listarPage(pageable);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- BUSCAR POR ID ---
    @Operation(summary = "Buscar Alumno por ID", description = "Obtiene los detalles de un Alumno específico usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<AlumnoAllResponse>> buscar(
            @RequestParam("idAlumno") Long idAlumno) {
        ObjectResponse<AlumnoAllResponse> response = service.buscar(idAlumno);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR (Individual) ---
    @Operation(summary = "Registrar Alumno", description = "Crea y registra un nuevo Alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alumno registrado exitosamente"),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<ObjectResponse<AlumnoAllResponse>> registrar(
            @Valid @RequestBody AlumnoCreateRequest request) {
        ObjectResponse<AlumnoAllResponse> response = service.registrar(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- REGISTRAR MÚLTIPLES ---
    @Operation(
            summary = "Registrar múltiples Alumnos",
            description = "Crea y registra múltiples Alumnos a partir de una lista de solicitudes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alumnos registrados exitosamente")
    })
    @PostMapping("/registrar-all")
    public ResponseEntity<ListResponse<AlumnoAllResponse>> registrarAll(
            @Valid @RequestBody List<AlumnoCreateRequest> requests) {
        ListResponse<AlumnoAllResponse> response = service.registrarAll(requests);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ACTUALIZAR ---
    @Operation(summary = "Actualizar Alumno", description = "Actualiza los datos de un Alumno existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "Correo ya en uso",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PutMapping("/actualizar/{idAlumno}")
    public ResponseEntity<ObjectResponse<AlumnoAllResponse>> actualizar(
            @PathVariable Long idAlumno,
            @Valid @RequestBody AlumnoUpdateRequest request) {
        ObjectResponse<AlumnoAllResponse> response = service.actualizar(idAlumno, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    // --- ELIMINAR ---
    @Operation(summary = "Eliminar Alumno", description = "Realiza la eliminación lógica de un Alumno (deshabilita su cuenta).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alumno eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @DeleteMapping("/eliminar/{idAlumno}")
    public ResponseEntity<ObjectResponse<String>> eliminar(@PathVariable Long idAlumno) {
        ObjectResponse<String> response = service.eliminar(idAlumno);
        return ResponseEntity.status(response.status()).body(response);
    }
}
