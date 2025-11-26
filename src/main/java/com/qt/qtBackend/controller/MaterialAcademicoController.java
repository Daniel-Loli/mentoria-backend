package com.qt.qtBackend.controller;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoAllResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoShortResponse;
import com.qt.qtBackend.service.interfaces.IMaterialAcademicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/material-academico")
@RequiredArgsConstructor
@Tag(name = "MaterialAcademico", description = "Endpoints para la gestión de material académico")
public class MaterialAcademicoController {

    private final IMaterialAcademicoService service;

    // ------------------------ REGISTRO ------------------------ //

    @Operation(summary = "Registrar material académico para una institución")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material registrado"),
            @ApiResponse(responseCode = "404", description = "Institución no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar-institucion/{idInstitucion}")
    public ResponseEntity<ObjectResponse<MaterialAcademicoAllResponse>> registrarMaterialInstitucion(
            @PathVariable Long idInstitucion,
            @RequestParam("file") MultipartFile file,
            @RequestParam("descripcion") String descripcion
    ) {
        var response = service.registrarMaterialAcademicoInstitucion(idInstitucion, file, descripcion);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Registrar material académico para UGEL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material registrado"),
            @ApiResponse(responseCode = "404", description = "UGEL no encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @PostMapping("/registrar-ugel")
    public ResponseEntity<ObjectResponse<MaterialAcademicoAllResponse>> registrarMaterialUgel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("descripcion") String descripcion
    ) {
        var response = service.registrarMaterialAcademicoUgel(file, descripcion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------ LISTAR ------------------------ //

    @Operation(summary = "Listar material académico de UGEL")
    @GetMapping("/listar-ugel")
    public ResponseEntity<ListResponse<MaterialAcademicoShortResponse>> listarMaterialUgel() {
        var response = service.listarMaterialUgel();
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(summary = "Listar material académico por institución")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listar material academico"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/listar-segun-institucion")
    public ResponseEntity<ListResponse<MaterialAcademicoShortResponse>> listarPorInstitucion(
            @RequestParam("idInstitucion") Long idInstitucion
    ) {
        var response = service.listarPorInstitucion(idInstitucion);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------ BUSCAR ------------------------ //

    @Operation(summary = "Buscar material académico por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material encontrado"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<ObjectResponse<MaterialAcademicoAllResponse>> buscar(
            @RequestParam("idMaterial") Long idMaterial
    ) {
        var response = service.buscarMaterial(idMaterial);
        return ResponseEntity.status(response.status()).body(response);
    }

    // ------------------------ ELIMINAR ------------------------ //

    @Operation(summary = "Eliminar material académico (soft delete + borrar archivo Azure)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material eliminado"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado",
                    content = @Content(schema = @Schema(implementation = ObjectResponse.class)))
    })
    @DeleteMapping("/eliminar/{idMaterial}")
    public ResponseEntity<ObjectResponse<String>> eliminar(
            @PathVariable("idMaterial") Long idMaterial
    ) {
        var response = service.eliminarMaterial(idMaterial);
        return ResponseEntity.status(response.status()).body(response);
    }
}
