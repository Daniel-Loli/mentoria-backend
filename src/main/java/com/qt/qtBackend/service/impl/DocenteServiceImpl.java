package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docente.DocenteCreateRequest;
import com.qt.qtBackend.dto.docente.DocenteUpdateRequest;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Docente;
import com.qt.qtBackend.model.DocenteInstitucion;
import com.qt.qtBackend.model.Rol;
import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.interfaces.IDocenteRepo;
import com.qt.qtBackend.repository.interfaces.IRolRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IDocenteService;
import com.qt.qtBackend.utils.GeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.qt.qtBackend.Enum.Message.CORREO_EN_USO;
import static com.qt.qtBackend.Enum.Modulo.ROL;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocenteServiceImpl
        extends CRUDImpl<Docente, Long>
        implements IDocenteService {

    private final IDocenteRepo docenteRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IRolRepo rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final IMapperService mapperService;

    @Override
    protected IDocenteRepo getRepo() {
        return docenteRepo;
    }

    // === CONTAR ===
    @Override
    public ObjectResponse<Integer> contar() {
        Integer contar = docenteRepo.contar();
        return new ObjectResponse<>(200, Modulo.DOCENTE.contar(), contar);
    }

    // === LISTAR ===
    @Override
    public ListResponse<DocenteAllResponse> listar() {
        List<DocenteAllResponse> lista = docenteRepo.listar()
                .stream()
                .sorted(Comparator.comparing(Docente::getCreatedAt).reversed())
                .map(mapperService::convDocenteAll)
                .collect(Collectors.toList());
        return new ListResponse<>(200, Modulo.DOCENTE.listado(), lista,lista.size());
    }

    // === LISTAR PAGINADO ===
    @Override
    public ListPageResponse<DocenteAllResponse> listarPage(Pageable pageable) {
        var page = docenteRepo.listarPage(pageable)
                .map(mapperService::convDocenteAll);
        return new ListPageResponse<>(
                200,
                Modulo.DOCENTE.listadoPage(),
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
    public ObjectResponse<DocenteAllResponse> buscar(Long idDocente) {
        Optional<Docente> docenteOpt = docenteRepo.buscarPorId(idDocente);
        if (docenteOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.DOCENTE.encontrado(),
                mapperService.convDocenteAll(docenteOpt.get()));
    }

    // === REGISTRAR ===
    @Override
    public ObjectResponse<DocenteAllResponse> registrar(DocenteCreateRequest request) {
        // === Validar correo único ===
        if (usuarioRepo.buscarPorEmail(request.getEmail()).isPresent()) {
            return new ObjectResponse<>(409, CORREO_EN_USO.toString(), null);
        }

        // === Validar documento duplicado ===
        Optional<Docente> duplicado = docenteRepo.buscarPorDocumento(
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

        // === Asignar rol DOCENTE ===
        RolEnum rolEnum = RolEnum.DOCENTE;
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

        // === Crear docente ===
        Docente docente = Docente.builder()
                .usuario(usuario)
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .tipoDocumento(request.getTipoDocumento())
                .docIdentidad(request.getDocIdentidad())
                .sexo(request.getSexo())
                .estado(EstadoDocenteEnum.ACTIVO)
                .enabled(true)
                .build();

        docenteRepo.save(docente);

        return new ObjectResponse<>(201, Modulo.DOCENTE.registrado(),
                mapperService.convDocenteAll(docente));
    }

    // === REGISTRAR MÚLTIPLES ===
    @Override
    public ListResponse<DocenteAllResponse> registrarAll(List<DocenteCreateRequest> requests) {
        List<DocenteAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (DocenteCreateRequest request : requests) {
            try {
                ObjectResponse<DocenteAllResponse> response = registrar(request);
                if (response.status() == 201 && response.data() != null) {
                    registrados.add(response.data());
                } else {
                    errorCount++;
                }
            } catch (Exception e) {
                log.error("Error registrando docente: {}", e.getMessage());
                errorCount++;
            }
        }

        return new ListResponse<>(
                201,
                Modulo.DOCENTE.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }

    // === ACTUALIZAR ===
    @Override
    public ObjectResponse<DocenteAllResponse> actualizar(Long idDocente, DocenteUpdateRequest request) {
        Optional<Docente> docenteOpt = docenteRepo.buscarPorId(idDocente);
        if (docenteOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
        }

        Docente docente = docenteOpt.get();
        Usuario usuario = docente.getUsuario();

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
            docente.setTipoDocumento(request.getTipoDocumento());
            docente.setDocIdentidad(request.getDocIdentidad());

        } else if (request.getTipoDocumento() != null) {
            return new ObjectResponse<>(400,
                    "Debe enviar también el número de documento para actualizar el tipo de documento.", null);

        } else if (request.getDocIdentidad() != null) {
            int longitudEsperada = docente.getTipoDocumento().getLongitudMaxima();
            if (request.getDocIdentidad().length() != longitudEsperada) {
                return new ObjectResponse<>(
                        400,
                        String.format("El número de documento debe tener exactamente %d caracteres según el tipo actual (%s)",
                                longitudEsperada, docente.getTipoDocumento().name()),
                        null
                );
            }
            docente.setDocIdentidad(request.getDocIdentidad());
        }

        if (request.getNombres() != null) docente.setNombres(request.getNombres());
        if (request.getApellidos() != null) docente.setApellidos(request.getApellidos());
        if (request.getSexo() != null) docente.setSexo(request.getSexo());

        docenteRepo.save(docente);

        return new ObjectResponse<>(200, Modulo.DOCENTE.actualizado(), mapperService.convDocenteAll(docente));
    }

    // === ELIMINAR ===
    @Override
    public ObjectResponse<String> eliminar(Long idDocente) {
        Optional<Docente> docenteOpt = docenteRepo.buscarPorId(idDocente);
        if (docenteOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
        }

        Docente docente = docenteOpt.get();
        docente.setEnabled(false);
        docenteRepo.save(docente);

        return new ObjectResponse<>(200, Modulo.DOCENTE.eliminado(), null);
    }
}
