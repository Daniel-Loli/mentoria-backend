package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.EstadoAlumnoEnum;
import com.qt.qtBackend.Enum.EstadoUsuarioEnum;
import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.RolEnum;
import com.qt.qtBackend.dto.alumno.AlumnoAllResponse;
import com.qt.qtBackend.dto.alumno.AlumnoCreateRequest;
import com.qt.qtBackend.dto.alumno.AlumnoUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Alumno;
import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.model.Rol;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.interfaces.IAlumnoRepo;
import com.qt.qtBackend.repository.interfaces.IRolRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IAlumnoService;
import com.qt.qtBackend.utils.GeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qt.qtBackend.Enum.Message.CORREO_EN_USO;
import static com.qt.qtBackend.Enum.Modulo.ROL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl
        extends CRUDImpl<Alumno, Long>
        implements IAlumnoService {

        private final IAlumnoRepo AlumnoRepo;
        private final IUsuarioRepo usuarioRepo;
        private final IRolRepo rolRepo;
        private final PasswordEncoder passwordEncoder;
        private final IMapperService mapperService;

        @Override
        protected IAlumnoRepo getRepo() {
            return AlumnoRepo;
        }

        // === CONTAR ===
        @Override
        public ObjectResponse<Integer> contar() {
            Integer contar = AlumnoRepo.contar();
            return new ObjectResponse<>(200, Modulo.ALUMNO.contar(), contar);
        }

        // === LISTAR ===
        @Override
        public ListResponse<AlumnoAllResponse> listar() {
            List<AlumnoAllResponse> lista = AlumnoRepo.listar()
                    .stream()
                    .sorted(Comparator.comparing(Alumno::getCreatedAt).reversed())
                    .map(mapperService::convAlumnoAll)
                    .collect(Collectors.toList());
            return new ListResponse<>(200, Modulo.ALUMNO.listado(), lista,lista.size());
        }

        // === LISTAR PAGINADO ===
        @Override
        public ListPageResponse<AlumnoAllResponse> listarPage(Pageable pageable) {
            var page = AlumnoRepo.listarPage(pageable)
                    .map(mapperService::convAlumnoAll);
            return new ListPageResponse<>(
                    200,
                    Modulo.ALUMNO.listadoPage(),
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isLast()
            );
        }

        // === BUSCAR ===
        @Override
        public ObjectResponse<AlumnoAllResponse> buscar(Long idAlumno) {
            Optional<Alumno> AlumnoOpt = AlumnoRepo.buscarPorId(idAlumno);
            if (AlumnoOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
            }
            return new ObjectResponse<>(200, Modulo.ALUMNO.encontrado(),
                    mapperService.convAlumnoAll(AlumnoOpt.get()));
        }

        // === REGISTRAR ===
        @Override
        public ObjectResponse<AlumnoAllResponse> registrar(AlumnoCreateRequest request) {
            // === Validar correo único ===
            if (usuarioRepo.buscarPorEmail(request.getEmail()).isPresent()) {
                return new ObjectResponse<>(409, CORREO_EN_USO.toString(), null);
            }

            // === Validar documento duplicado ===
            Optional<Alumno> duplicado = AlumnoRepo.buscarPorDocumento(
                    request.getTipoDocumento(),
                    request.getDocIdentidad()
            );
            if (duplicado.isPresent()) {
                return new ObjectResponse<>(409, "Documento de identidad ya existente", null);
            }

            // === Validar longitud del documento ===
            int longitudEsperada = request.getTipoDocumento().getLongitudMaxima();
            if (request.getDocIdentidad().length() != longitudEsperada) {
                return new ObjectResponse<>(
                        400,
                        String.format("El número de documento debe tener exactamente %d caracteres para el tipo %s",
                                longitudEsperada, request.getTipoDocumento().name()),
                        null
                );
            }

            // === Asignar rol Alumno ===
            RolEnum rolEnum = RolEnum.ALUMNO;
            Optional<Rol> rolOpt = rolRepo.buscarPorId(rolEnum.getValor());
            if (rolOpt.isEmpty()) {
                return new ObjectResponse<>(404, ROL.noEncontrado(), null);
            }
            Rol rolUsuario = rolOpt.get();

            // === Generar código único ===
            String codigo;
            do {
                codigo = "US" + GeneratorUtil.generarCodigoNumerico(6);
            } while (usuarioRepo.existePorCodigo(codigo));

            // === Crear usuario ===
            Usuario usuario = Usuario.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .codigo(codigo)
                    .telefono(request.getTelefono())
                    .rol(rolUsuario)
                    .estado(EstadoUsuarioEnum.ACTIVO)
                    .build();
            usuarioRepo.save(usuario);

            // === Crear Alumno ===
            Alumno alumno = Alumno.builder()
                    .usuario(usuario)
                    .nombres(request.getNombres())
                    .apellidos(request.getApellidos())
                    .tipoDocumento(request.getTipoDocumento())
                    .docIdentidad(request.getDocIdentidad())
                    .sexo(request.getSexo())
                    .estado(EstadoAlumnoEnum.ACTIVO)
                    .enabled(true)
                    .build();

            AlumnoRepo.save(alumno);

            return new ObjectResponse<>(201, Modulo.ALUMNO.registrado(),
                    mapperService.convAlumnoAll(alumno));
        }

        // === REGISTRAR MÚLTIPLES ===
        @Override
        public ListResponse<AlumnoAllResponse> registrarAll(List<AlumnoCreateRequest> requests) {
            List<AlumnoAllResponse> registrados = new ArrayList<>();
            int errorCount = 0;

            for (AlumnoCreateRequest request : requests) {
                try {
                    ObjectResponse<AlumnoAllResponse> response = registrar(request);
                    if (response.status() == 201 && response.data() != null) {
                        registrados.add(response.data());
                    } else {
                        errorCount++;
                    }
                } catch (Exception e) {
                    log.error("Error registrando Alumno: {}", e.getMessage());
                    errorCount++;
                }
            }

            return new ListResponse<>(
                    201,
                    Modulo.ALUMNO.resumenAllRegistro(registrados.size(), errorCount),
                    registrados,
                    registrados.size()
            );
        }

        // === ACTUALIZAR ===
        @Override
        public ObjectResponse<AlumnoAllResponse> actualizar(Long idAlumno, AlumnoUpdateRequest request) {
            Optional<Alumno> AlumnoOpt = AlumnoRepo.buscarPorId(idAlumno);
            if (AlumnoOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
            }

            Alumno Alumno = AlumnoOpt.get();
            Usuario usuario = Alumno.getUsuario();

            // === Validar correo único ===
            if (request.getEmail() != null) {
                Optional<Usuario> existingUserOpt = usuarioRepo.buscarPorEmail(request.getEmail());
                if (existingUserOpt.isPresent() && !existingUserOpt.get().getIdUsuario().equals(usuario.getIdUsuario())) {
                    return new ObjectResponse<>(409, CORREO_EN_USO.toString(), null);
                }
                usuario.setEmail(request.getEmail());
            }

            // === Actualizar teléfono ===
            if (request.getTelefono() != null) usuario.setTelefono(request.getTelefono());
            usuarioRepo.save(usuario);

            // === Validar tipoDocumento / docIdentidad ===
            if (request.getTipoDocumento() != null && request.getDocIdentidad() != null) {
                int longitudEsperada = request.getTipoDocumento().getLongitudMaxima();
                if (request.getDocIdentidad().length() != longitudEsperada) {
                    return new ObjectResponse<>(
                            400,
                            String.format("El número de documento debe tener exactamente %d caracteres para el tipo %s",
                                    longitudEsperada, request.getTipoDocumento().name()),
                            null
                    );
                }
                Alumno.setTipoDocumento(request.getTipoDocumento());
                Alumno.setDocIdentidad(request.getDocIdentidad());

            } else if (request.getTipoDocumento() != null) {
                return new ObjectResponse<>(400,
                        "Debe enviar también el número de documento para actualizar el tipo de documento.", null);

            } else if (request.getDocIdentidad() != null) {
                int longitudEsperada = Alumno.getTipoDocumento().getLongitudMaxima();
                if (request.getDocIdentidad().length() != longitudEsperada) {
                    return new ObjectResponse<>(
                            400,
                            String.format("El número de documento debe tener exactamente %d caracteres según el tipo actual (%s)",
                                    longitudEsperada, Alumno.getTipoDocumento().name()),
                            null
                    );
                }
                Alumno.setDocIdentidad(request.getDocIdentidad());
            }

            if (request.getNombres() != null) Alumno.setNombres(request.getNombres());
            if (request.getApellidos() != null) Alumno.setApellidos(request.getApellidos());
            if (request.getSexo() != null) Alumno.setSexo(request.getSexo());

            AlumnoRepo.save(Alumno);

            return new ObjectResponse<>(200, Modulo.ALUMNO.actualizado(), mapperService.convAlumnoAll(Alumno));
        }

        // === ELIMINAR ===
        @Override
        public ObjectResponse<String> eliminar(Long idAlumno) {
            Optional<Alumno> alumnoOpt = AlumnoRepo.buscarPorId(idAlumno);
            if (alumnoOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
            }

            Alumno alumno = alumnoOpt.get();
            alumno.setEnabled(false);
            AlumnoRepo.save(alumno);

            return new ObjectResponse<>(200, Modulo.ALUMNO.eliminado(), null);
        }
    }
