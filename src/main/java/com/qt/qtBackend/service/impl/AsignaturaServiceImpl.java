package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.asignatura.AsignaturaAllResponse;
import com.qt.qtBackend.dto.asignatura.AsignaturaCreateRequest;
import com.qt.qtBackend.dto.asignatura.AsignaturaUpdateRequest;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAsignaturaRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IAsignaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsignaturaServiceImpl
        extends CRUDImpl<Asignatura, Long>
        implements IAsignaturaService {

    private final IAsignaturaRepo asignaturaRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<Asignatura, Long> getRepo() {
        return asignaturaRepo;
    }

    // === CREAR OBJETO DESDE REQUEST ===
    private Asignatura crearEntidadDesdeRequest(AsignaturaCreateRequest request) {
        return Asignatura.builder()
                .nombre(request.getNombre())
                .build();
    }

    // === CONTAR ===
    @Override
    public ObjectResponse<Integer> contar() {
        Integer total = asignaturaRepo.contar();
        return new ObjectResponse<>(200, Modulo.ASIGNATURA.contar(), total);
    }

    // === LISTAR ===
    @Override
    public ListResponse<AsignaturaAllResponse> listar() {
        List<Asignatura> asignaturas = asignaturaRepo.listar();
        List<AsignaturaAllResponse> data = asignaturas.stream()
                .sorted(Comparator.comparing(Asignatura::getCreatedAt).reversed())
                .map(mapperService::convAsignaturaAll)
                .toList();

        return new ListResponse<>(200, Modulo.ASIGNATURA.listado(), data,data.size());
    }

    // === LISTAR PAGINADO ===
    @Override
    public ListPageResponse<AsignaturaAllResponse> listarPaginado(Pageable pageable) {
        Page<Asignatura> page = asignaturaRepo.listarPage(pageable);

        List<AsignaturaAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convAsignaturaAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.ASIGNATURA.listadoPage(),
                data,
                page.getNumber(),         // Página actual
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Es la última página
        );
    }


    // === BUSCAR POR ID ===
    @Override
    public ObjectResponse<AsignaturaAllResponse> buscar(Long idAsignatura) {
        Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(idAsignatura);
        if (asignaturaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
        }

        return new ObjectResponse<>(
                200,
                Modulo.ASIGNATURA.encontrado(),
                mapperService.convAsignaturaAll(asignaturaOpt.get())
        );
    }

    // === REGISTRAR ===
    @Override
    public ObjectResponse<AsignaturaAllResponse> registrar(AsignaturaCreateRequest request) {
        // Validar duplicado
        Optional<Asignatura> duplicado = asignaturaRepo.buscarPorNombre(request.getNombre());
        if (duplicado.isPresent()) {
            return new ObjectResponse<>(409, "La asignatura ya existe", null);
        }

        Asignatura nueva = crearEntidadDesdeRequest(request);
        asignaturaRepo.save(nueva);

        return new ObjectResponse<>(201, Modulo.ASIGNATURA.registrado(), mapperService.convAsignaturaAll(nueva));
    }

    // === REGISTRAR VARIAS ===
    @Override
    public ListResponse<AsignaturaAllResponse> registrarAll(List<AsignaturaCreateRequest> requests) {
        List<Asignatura> nuevas = new ArrayList<>();
        List<AsignaturaAllResponse> registradas = new ArrayList<>();
        int errorCount = 0;

        for (AsignaturaCreateRequest request : requests) {
            try {
                Optional<Asignatura> duplicado = asignaturaRepo.buscarPorNombre(request.getNombre());
                if (duplicado.isEmpty()) {
                    nuevas.add(crearEntidadDesdeRequest(request));
                } else {
                    errorCount++;
                }
            } catch (Exception e) {
                log.error("Error creando asignatura: {}", e.getMessage());
                errorCount++;
            }
        }

        if (!nuevas.isEmpty()) {
            List<Asignatura> guardadas = asignaturaRepo.saveAll(nuevas);
            registradas = guardadas.stream()
                    .map(mapperService::convAsignaturaAll)
                    .toList();
        }

        return new ListResponse<>(
                201,
                Modulo.ASIGNATURA.resumenAllRegistro(registradas.size(), errorCount),
                registradas,
                registradas.size()
        );
    }

    // === ACTUALIZAR ===
    @Override
    public ObjectResponse<AsignaturaAllResponse> actualizar(Long idAsignatura, AsignaturaUpdateRequest request) {
        Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(idAsignatura);
        if (asignaturaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
        }

        Asignatura asignatura = asignaturaOpt.get();

        // Validar duplicado si el nombre cambia
        if (request.getNombre() != null && !request.getNombre().equalsIgnoreCase(asignatura.getNombre())) {
            Optional<Asignatura> duplicado = asignaturaRepo.buscarPorNombre(request.getNombre());
            if (duplicado.isPresent()) {
                return new ObjectResponse<>(409, "La asignatura ya existe", null);
            }
            asignatura.setNombre(request.getNombre());
        }

        asignaturaRepo.save(asignatura);
        return new ObjectResponse<>(200, Modulo.ASIGNATURA.actualizado(), mapperService.convAsignaturaAll(asignatura));
    }

    // === ELIMINAR ===
    @Override
    public ObjectResponse<String> eliminar(Long idAsignatura) {
        Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(idAsignatura);
        if (asignaturaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
        }

        Asignatura asignatura = asignaturaOpt.get();
        asignatura.setEnabled(false);
        asignaturaRepo.save(asignatura);

        return new ObjectResponse<>(200, Modulo.ASIGNATURA.eliminado(), null);
    }
}
