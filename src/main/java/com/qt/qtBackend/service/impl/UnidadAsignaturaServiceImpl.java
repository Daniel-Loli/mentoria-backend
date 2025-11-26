package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.misionAsignatura.MAsignaturaShortResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaAllResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaCreateRequest;
import com.qt.qtBackend.dto.unidad.UnidadShortResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaShortResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.model.MisionAsignatura;
import com.qt.qtBackend.model.Unidad;
import com.qt.qtBackend.model.UnidadAsignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAsignaturaRepo;
import com.qt.qtBackend.repository.interfaces.IUnidadAsignaturaRepo;
import com.qt.qtBackend.repository.interfaces.IUnidadRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IUnidadAsignaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnidadAsignaturaServiceImpl
        extends CRUDImpl<UnidadAsignatura, Long>
        implements IUnidadAsignaturaService {

    private final IUnidadAsignaturaRepo unidadAsignaturaRepo;
    private final IUnidadRepo unidadRepo;
    private final IAsignaturaRepo asignaturaRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<UnidadAsignatura, Long> getRepo() {
        return unidadAsignaturaRepo;
    }

    @Override
    public ObjectResponse<UnidadAsignaturaAllResponse> buscar(Long idUnidadAsignatura) {
        Optional<UnidadAsignatura> opt = unidadAsignaturaRepo.buscarPorId(idUnidadAsignatura);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.UNIDAD_ASIGNATURA.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.UNIDAD_ASIGNATURA.encontrado(),
                mapperService.convUnidadAsignaturaAll(opt.get()));
    }

    @Override
    public ListResponse<UnidadAsignaturaShortResponse> listarPorUnidad(Long idUnidad) {
        Optional<Unidad> unidadOpt = unidadRepo.buscarPorId(idUnidad);
        if(unidadOpt.isEmpty()){
            return new ListResponse<>(
                    404, Modulo.UNIDAD.noEncontrado(),null,null
            );
        }
        List<UnidadAsignaturaShortResponse> lista = unidadAsignaturaRepo.listarPorUnidad(idUnidad)
                .stream()
                .sorted(Comparator.comparing(UnidadAsignatura::getCreatedAt))
                .map(mapperService::convUnidadAsignaturaShort)
                .toList();

        return new ListResponse<>(
                200,
                Modulo.UNIDAD_ASIGNATURA.listado(),
                lista,
                lista.size()
        );
    }

    @Override
    public ObjectResponse<UnidadAsignaturaAllResponse> registrar(UnidadAsignaturaCreateRequest request) {
        Optional<Unidad> unidadOpt = unidadRepo.buscarPorId(request.getIdUnidad());
        if (unidadOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.UNIDAD.noEncontrado(), null);
        }
        Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(request.getIdAsignatura());
        if (asignaturaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
        }

        UnidadAsignatura unidadAsignatura =  UnidadAsignatura.builder()
                .unidad(unidadOpt.get())
                .asignatura(asignaturaOpt.get())
                .build();
        unidadAsignaturaRepo.save(unidadAsignatura);
        return new ObjectResponse<>(201, Modulo.UNIDAD_ASIGNATURA.registrado(),
                mapperService.convUnidadAsignaturaAll(unidadAsignatura));
    }

    @Override
    public ListResponse<UnidadAsignaturaAllResponse> registrarAll(List<UnidadAsignaturaCreateRequest> requests) {
        List<UnidadAsignaturaAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (UnidadAsignaturaCreateRequest request : requests) {
            try {
                ObjectResponse<UnidadAsignaturaAllResponse> response = registrar(request);
                if (response.status() == 201 && response.data() != null) {
                    registrados.add(response.data());
                } else {
                    errorCount++;
                }
            } catch (Exception e) {
                log.error("Error registrando: {}", e.getMessage());
                errorCount++;
            }
        }

        return new ListResponse<>(
                201,
                Modulo.UNIDAD_ASIGNATURA.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }

    @Override
    public ObjectResponse<String> eliminar(Long idUnidadAsignatura) {
        Optional<UnidadAsignatura> opt = unidadAsignaturaRepo.buscarPorId(idUnidadAsignatura);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.UNIDAD_ASIGNATURA.noEncontrado(), null);
        }
        UnidadAsignatura unidadAsignatura = opt.get();
        unidadAsignatura.setEnabled(false);
        unidadAsignaturaRepo.save(unidadAsignatura);
        return new ObjectResponse<>(200, Modulo.UNIDAD_ASIGNATURA.eliminado(), null);
    }
}
