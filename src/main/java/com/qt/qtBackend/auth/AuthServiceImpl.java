package com.qt.qtBackend.auth;

import com.qt.qtBackend.Enum.EstadoUsuarioEnum;
import com.qt.qtBackend.Enum.RolEnum;
import com.qt.qtBackend.auth.dto.*;
import com.qt.qtBackend.dto.alumno.AlumnoAllResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.institucion.InstitucionAllResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.*;
import com.qt.qtBackend.repository.interfaces.*;
import com.qt.qtBackend.security.JwtTokenUtil;
import com.qt.qtBackend.utils.GeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.qt.qtBackend.Enum.Message.*;
import static com.qt.qtBackend.Enum.Modulo.ROL;
import static com.qt.qtBackend.Enum.Modulo.USUARIO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl
        implements IAuthService{
    private final IUsuarioRepo userRepo;
    private final IDocenteRepo docenteRepo;
    private final IAlumnoRepo alumnoRepo;
    private final IInstitucionRepo institucionRepo;
    private final IRolRepo rolRepo;
    private final IMapperService mapperService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    private void authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (DisabledException e) {
            log.error("Usuario deshabilitado: {}", request.getEmail());
            throw new DisabledException("Usuario deshabilitado", e);
        } catch (BadCredentialsException e) {
            log.error("Credenciales inválidas para: {}", request.getEmail());
            throw new BadCredentialsException("Credenciales inválidas", e);
        }
    }
    @Override
    public ObjectResponse<AuthResponse> login(LoginRequest request) {
        // 1. Autenticar usuario
        authenticate(request);

        // 2. Cargar detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Generar token
        final String token = jwtTokenUtil.generateToken(userDetails);
        Optional<Usuario> optionalUser = userRepo.buscarPorEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new ObjectResponse<>(CORREO_NO_ENCONTRADO.getStatus(), CORREO_NO_ENCONTRADO.toString(), null);
        }
        Usuario usuario = optionalUser.get();
        UsuarioAllResponse usuarioAllResponse = mapperService.convUsuarioAll(usuario);

        // 5. Validar rol del usuario
        Long idRol = usuario.getRol().getIdRol();
        RolEnum rol = RolEnum.fromValor(idRol);

        if (rol == null) {
            return new ObjectResponse<>(404, "Usuario no cuenta con un rol válido", null);
        }
        InstitucionAuthResponse institucionAuthResponse =null;
        DocenteAuthResponse docenteAuthResponse=null;
        UgelAuthResponse ugelAuthResponse =null;
        AlumnoAuthResponse alumnoAuthResponse = null;
        switch (rol){
            case UGEL:
                ugelAuthResponse = new UgelAuthResponse(
                        "Ugel 05",
                        "Jirón Caravelí, Breña, Breña 15083, Perú",
                        BigDecimal.valueOf(-12.0587189),
                        BigDecimal.valueOf(-77.0502426)
                );
                break;
            case INSTITUCION:
                Institucion institucion = institucionRepo.buscarPorIdUsuario(usuario.getIdUsuario()).orElse(null);
                if(institucion !=null){
                    institucionAuthResponse = mapperService.convInstitucionAuth(institucion);
                }
                break;
            case DOCENTE: // Docente
                Docente docente = docenteRepo.buscarPorIdUsuario(usuario.getIdUsuario()).orElse(null);
                if(docente !=null){
                    docenteAuthResponse = mapperService.convDocenteAuth(docente);
                }
                break;
            case ALUMNO:
                Alumno alumno =  alumnoRepo.buscarPorIdUsuario(usuario.getIdUsuario()).orElse(null);
                if(alumno !=null){
                    alumnoAuthResponse = mapperService.convAlumnoAuth(alumno);
                }
                break;

        }
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .usuario(usuarioAllResponse)
                .ugel(ugelAuthResponse)
                .institucion(institucionAuthResponse)
                .docente(docenteAuthResponse)
                .alumno(alumnoAuthResponse)
                .build();

        return new ObjectResponse<>(LOGIN_ACCESS.getStatus(), LOGIN_ACCESS.toString(), authResponse);
    }

    //no se usara
    public ObjectResponse<AuthResponse> registerAdmin(RegisterAdminRequest request) {

        if (userRepo.buscarPorEmail(request.getEmail()).isPresent()) {
            return new ObjectResponse<>(CORREO_EN_USO.getStatus(), CORREO_EN_USO.toString(), null);
        }
        RolEnum rol = RolEnum.UGEL;
        Optional<Rol> rolOpt = rolRepo.buscarPorId(rol.getValor());
        if (rolOpt.isEmpty()) {
            return new ObjectResponse<>(404,ROL.noEncontrado(), null);
        }
        Rol rolUsuario = rolOpt.get();
        String codigo;
        do {
            codigo = "US"+GeneratorUtil.generarCodigoNumerico(6);
        } while (userRepo.existePorCodigo(codigo));

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .codigo(codigo)
                .telefono(request.getTelefono())
                .rol(rolUsuario)
                .estado(EstadoUsuarioEnum.ACTIVO)
                .build();

        userRepo.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        UgelAuthResponse ugelAuthResponse = new UgelAuthResponse(
                "Ugel 05",
                "Jirón Caravelí, Breña, Breña 15083, Perú",
                BigDecimal.valueOf(-12.0587189),
                BigDecimal.valueOf(-77.0502426)
        );
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .usuario(mapperService.convUsuarioAll(usuario))
                .ugel(ugelAuthResponse)
                .institucion(null)
                .docente(null)
                .build();


        return new ObjectResponse<>(201, USUARIO.registrado(), authResponse);
    }
    public ObjectResponse<AuthResponse> registerAgente(RegistrarAgenteRequest request) {


        if (userRepo.buscarPorEmail(request.getEmail()).isPresent()) {
            return new ObjectResponse<>(CORREO_EN_USO.getStatus(), CORREO_EN_USO.toString(), null);
        }
        RolEnum rol = RolEnum.AGENTE;
        Optional<Rol> rolOpt = rolRepo.buscarPorId(rol.getValor());
        if (rolOpt.isEmpty()) {
            return new ObjectResponse<>(404,ROL.noEncontrado(), null);
        }
        Rol rolUsuario = rolOpt.get();
        String codigo;
        do {
            codigo = "AG"+GeneratorUtil.generarCodigoNumerico(6);
        } while (userRepo.existePorCodigo(codigo));

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .codigo(codigo)
                .rol(rolUsuario)
                .estado(EstadoUsuarioEnum.ACTIVO)
                .build();

        userRepo.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);


        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .usuario(mapperService.convUsuarioAll(usuario))
                .institucion(null)
                .docente(null)
                .build();


        return new ObjectResponse<>(201, USUARIO.registrado(), authResponse);
    }



}
