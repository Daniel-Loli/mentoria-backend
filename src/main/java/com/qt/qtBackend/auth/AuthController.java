package com.qt.qtBackend.auth;

import com.qt.qtBackend.auth.dto.AuthResponse;
import com.qt.qtBackend.auth.dto.LoginRequest;
import com.qt.qtBackend.auth.dto.RegisterAdminRequest;
import com.qt.qtBackend.auth.dto.RegistrarAgenteRequest;
import com.qt.qtBackend.dto.base.MessageResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.model.Mensaje;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.qt.qtBackend.Enum.Message.CREDENCIALES_INCORRECTAS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints para autenticación")
public class AuthController {

    private final IAuthService service;

    @Operation(
            summary = "Login",
            description = "Valida las credenciales de un usuario y devuelve un token JWT junto con la información básica del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "404", description = "Correo no encontrado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ObjectResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        ObjectResponse<AuthResponse> response = service.login(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @Operation(
            summary = "Registrar administrador",
            description = "Registra un nuevo usuario con rol de administrador en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario administrador registrado exitosamente")
    })
    @PostMapping("/register-admin")
    public ResponseEntity<ObjectResponse<AuthResponse>> registerUserAdmin(
            @Valid @RequestBody RegisterAdminRequest request) {
        ObjectResponse<AuthResponse> response = service.registerAdmin(request);
        return ResponseEntity.status(response.status()).body(response);
    }
    @PostMapping("/register-agente")
    public ResponseEntity<ObjectResponse<AuthResponse>> registerUserAgente(
            @Valid @RequestBody RegistrarAgenteRequest request) {
        ObjectResponse<AuthResponse> response = service.registerAgente(request);
        return ResponseEntity.status(response.status()).body(response);
    }

}
