package com.qt.qtBackend.service.impl;


import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.misionAsignatura.MAsignaturaShortResponse;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaCreateRequest;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaShortResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.model.Mision;
import com.qt.qtBackend.model.MisionAsignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAsignaturaRepo;
import com.qt.qtBackend.repository.interfaces.IMisionAsignaturaRepo;
import com.qt.qtBackend.repository.interfaces.IMisionRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IMisionAsignaturaService;
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
public class MisionAsignaturaServiceImpl
        extends CRUDImpl<MisionAsignatura, Long>
        implements IMisionAsignaturaService {

        private final IMapperService mapperService;
        private final IMisionAsignaturaRepo MisionAsignaturaRepo;
        private final IMisionRepo MisionRepo;
        private final IAsignaturaRepo asignaturaRepo;

        @Override
        protected IGenericRepo<MisionAsignatura, Long> getRepo() {
            return MisionAsignaturaRepo;
        }

        @Override
        public ObjectResponse<Integer> contarSegunMision(Long idMision) {
            Optional<Mision> MisionPpt = MisionRepo.buscarPorId(idMision);
            if (MisionPpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
            }
            Integer total = MisionAsignaturaRepo.contarSegunMision(idMision);
            return new ObjectResponse<>(200, Modulo.MISION_ASIGNATURA.contar(), total);
        }

        @Override
        public ListResponse<MAsignaturaShortResponse> listarSegunMision(Long idMision) {
            Optional<Mision> MisionPpt = MisionRepo.buscarPorId(idMision);
            if (MisionPpt.isEmpty()) {
                return new ListResponse<>(404, Modulo.MISION.noEncontrado(), null,null);
            }
            List<MAsignaturaShortResponse> lista = MisionAsignaturaRepo.listarSegunMision(idMision)
                    .stream()
                    .sorted(Comparator.comparing(MisionAsignatura::getCreatedAt).reversed())
                    .map(mapperService::convMAsignaturaShort)
                    .toList();

            return new ListResponse<>(200, Modulo.MISION_ASIGNATURA.listado(), lista,lista.size());
        }

        @Override
        public ListPageResponse<MAsignaturaShortResponse> listarPageSegunMision(Pageable pageable, Long idMision) {
            Page<MisionAsignatura> page = MisionAsignaturaRepo.listarPageSegunMision(pageable, idMision);

            List<MAsignaturaShortResponse> data = page.getContent()
                    .stream()
                    .map(mapperService::convMAsignaturaShort)
                    .toList();

            return new ListPageResponse<>(
                    200,
                    Modulo.MISION_ASIGNATURA.listadoPage(),
                    data,
                    page.getNumber(),         // Página actual
                    page.getSize(),           // Tamaño de página
                    page.getTotalElements(),  // Total de registros
                    page.getTotalPages(),     // Total de páginas
                    page.isLast()             // Es la última página
            );
        }

        @Override
        public ObjectResponse<MisionAsignaturaShortResponse> buscar(Long idMisionAsignatura) {
            Optional<MisionAsignatura> opt = MisionAsignaturaRepo.buscarPorId(idMisionAsignatura);
            if (opt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.MISION_ASIGNATURA.noEncontrado(), null);
            }

            return new ObjectResponse<>(
                    200,
                    Modulo.MISION_ASIGNATURA.encontrado(),
                    mapperService.convMisionAsignaturaShort(opt.get()));
        }

        @Override
        public ObjectResponse<MisionAsignaturaShortResponse> registrar(MisionAsignaturaCreateRequest request) {
            Optional<Mision> misionOpt = MisionRepo.buscarPorId(request.getIdMision());
            if (misionOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
            }
            Mision mision = misionOpt.get();


            Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(request.getIdAsignatura());
            if (asignaturaOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
            }
            Asignatura asignatura = asignaturaOpt.get();

            MisionAsignatura nuevo = MisionAsignatura.builder()
                    .mision(mision)
                    .asignatura(asignatura)
                    .build();

            MisionAsignatura guardado = MisionAsignaturaRepo.save(nuevo);
            return new ObjectResponse<>(
                    201,
                    Modulo.MISION_ASIGNATURA.registrado(),
                    mapperService.convMisionAsignaturaShort(guardado)
            );
        }

        @Override
        public ListResponse<MisionAsignaturaShortResponse> registrarAll(List<MisionAsignaturaCreateRequest> requests) {
            List<MisionAsignaturaShortResponse> registrados = new ArrayList<>();
            int errorCount = 0;

            for (MisionAsignaturaCreateRequest request : requests) {
                try {
                    ObjectResponse<MisionAsignaturaShortResponse> response = registrar(request);
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
                    Modulo.MISION_ASIGNATURA.resumenAllRegistro(registrados.size(), errorCount),
                    registrados,
                    registrados.size()
            );
        }

        @Override
        public ObjectResponse<String> eliminar(Long idMisionAsignatura) {
            Optional<MisionAsignatura> opt = MisionAsignaturaRepo.buscarPorId(idMisionAsignatura);
            if (opt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.MISION_ASIGNATURA.noEncontrado(), null);
            }

            MisionAsignatura entidad = opt.get();
            entidad.setEnabled(false);
            MisionAsignaturaRepo.save(entidad);

            return new ObjectResponse<>(200, Modulo.MISION_ASIGNATURA.eliminado(), null);
        }
    }
