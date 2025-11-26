package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.EstadoDocenteInstitucion;
import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docente.DocenteCreateRequest;
import com.qt.qtBackend.dto.docenteInstitucion.DInstitucionAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteIAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionCreateRequest;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionUpdateRequest;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Conversacion;
import com.qt.qtBackend.model.Docente;
import com.qt.qtBackend.model.DocenteInstitucion;
import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IDocenteInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IDocenteRepo;
import com.qt.qtBackend.repository.interfaces.IInstitucionRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IDocenteInstitucionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocenteInstitucionServiceImpl
        extends CRUDImpl<DocenteInstitucion, Long>
        implements IDocenteInstitucionService {

    private final IDocenteInstitucionRepo docenteInstitucionRepo;
    private final IDocenteRepo docenteRepo;
    private final IInstitucionRepo institucionRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<DocenteInstitucion, Long> getRepo() {
        return docenteInstitucionRepo;
    }

    // ========================= CONTAR =========================
    @Override
    public ObjectResponse<Integer> contar() {
        Integer total = docenteInstitucionRepo.contar();
        return new ObjectResponse<>(200, Modulo.DOCENTE_INSTITUCION.contar(), total);
    }

    @Override
    public ObjectResponse<Integer> contarSegunDocente(Long idDocente) {
        Optional<Docente> docentePpt = docenteRepo.buscarPorId(idDocente);
        if (docentePpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
        }
        Integer total = docenteInstitucionRepo.contarSegunDocente(idDocente);
        return new ObjectResponse<>(200, Modulo.DOCENTE_INSTITUCION.contar(), total);
    }

    @Override
    public ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Integer total = docenteInstitucionRepo.contarSegunInstitucion(idInstitucion);
        return new ObjectResponse<>(200, Modulo.DOCENTE_INSTITUCION.contar(), total);
    }


    // ========================= LISTAR =========================
    @Override
    public ListResponse<DocenteInstitucionAllResponse> listar() {
        List<DocenteInstitucionAllResponse> lista = docenteInstitucionRepo.listar()
                .stream()
                .sorted(Comparator.comparing(DocenteInstitucion::getCreatedAt).reversed())
                .map(mapperService::convDocenteInstitucionAll)
                .toList();

        return new ListResponse<>(200, Modulo.DOCENTE_INSTITUCION.listado(), lista,lista.size());
    }

    @Override
    public ListResponse<DInstitucionAllResponse> listarSegunDocente(Long idDocente) {
        Optional<Docente> docentePpt = docenteRepo.buscarPorId(idDocente);
        if (docentePpt.isEmpty()) {
            return new ListResponse<>(404, Modulo.DOCENTE.noEncontrado(), null,null);
        }
        List<DInstitucionAllResponse> lista = docenteInstitucionRepo.listarSegunDocente(idDocente)
                .stream()
                .sorted(Comparator.comparing(DocenteInstitucion::getCreatedAt).reversed())
                .map(mapperService::convDInstitucionAll)
                .toList();

        return new ListResponse<>(200, Modulo.DOCENTE_INSTITUCION.listado(), lista,lista.size());
    }

    @Override
    public ListResponse<DocenteIAllResponse> listarSegunInstitucion(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ListResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null,null);
        }
        List<DocenteIAllResponse> lista = docenteInstitucionRepo.listarSegunInstitucion(idInstitucion)
                .stream()
                .sorted(Comparator.comparing(DocenteInstitucion::getCreatedAt).reversed())
                .map(mapperService::convDocenteIAll)
                .toList();

        return new ListResponse<>(200, Modulo.DOCENTE_INSTITUCION.listado(), lista,lista.size());
    }

    // ========================= LISTAR PAGINADO =========================
    @Override
    public ListPageResponse<DocenteInstitucionAllResponse> listarPage(Pageable pageable) {
        Page<DocenteInstitucion> page = docenteInstitucionRepo.listarPage(pageable);

        List<DocenteInstitucionAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convDocenteInstitucionAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.DOCENTE_INSTITUCION.listadoPage(),
                data,
                page.getNumber(),         // Página actual
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Indica si es la última página
        );
    }

    @Override
    public ListPageResponse<DInstitucionAllResponse> listarPageSegunDocente(Pageable pageable, Long idDocente) {
        Page<DocenteInstitucion> page = docenteInstitucionRepo.listarPageSegunDocente(pageable, idDocente);

        List<DInstitucionAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convDInstitucionAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.DOCENTE_INSTITUCION.listadoPage(),
                data,
                page.getNumber(),         // Página actual
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Es la última página
        );
    }

    @Override
    public ListPageResponse<DocenteIAllResponse> listarPageSegunInstitucion(Pageable pageable, Long idInstitucion) {
        Page<DocenteInstitucion> page = docenteInstitucionRepo.listarPageSegunInstitucion(pageable, idInstitucion);

        List<DocenteIAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convDocenteIAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.DOCENTE_INSTITUCION.listadoPage(),
                data,
                page.getNumber(),         // Página actual (0-based)
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Indica si es la última página
        );
    }


    // ========================= BUSCAR =========================
    @Override
    public ObjectResponse<DocenteInstitucionAllResponse> buscar(Long idDocenteInstitucion) {
        Optional<DocenteInstitucion> opt = docenteInstitucionRepo.buscarPorId(idDocenteInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE_INSTITUCION.noEncontrado(), null);
        }

        return new ObjectResponse<>(
                200,
                Modulo.DOCENTE_INSTITUCION.encontrado(),
                mapperService.convDocenteInstitucionAll(opt.get()));
    }

    // ========================= REGISTRAR =========================
    @Override
    @Transactional
    public ObjectResponse<DocenteInstitucionAllResponse> registrar(DocenteInstitucionCreateRequest request) {
        Optional<Docente> docentePpt = docenteRepo.buscarPorId(request.getIdDocente());
        if (docentePpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
        }
        Docente docente = docentePpt.get();


        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(request.getIdInstitucion());
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Institucion institucion = institucionOpt.get();

        DocenteInstitucion nuevo = DocenteInstitucion.builder()
                .docente(docente)
                .institucion(institucion)
                .estado(EstadoDocenteInstitucion.ACTIVO)
                .build();

        DocenteInstitucion guardado = docenteInstitucionRepo.save(nuevo);
        return new ObjectResponse<>(
                201,
                Modulo.DOCENTE_INSTITUCION.registrado(),
                mapperService.convDocenteInstitucionAll(guardado)
        );
    }
    @Override
    public ListResponse<DocenteInstitucionAllResponse> registrarAll(List<DocenteInstitucionCreateRequest> requests) {
        List<DocenteInstitucionAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (DocenteInstitucionCreateRequest request : requests) {
            try {
                ObjectResponse<DocenteInstitucionAllResponse> response = registrar(request);
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
                Modulo.DOCENTE_INSTITUCION.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }


    // ========================= ACTUALIZAR =========================
    @Override
    @Transactional
    public ObjectResponse<DocenteInstitucionAllResponse> actualizar(Long idDocenteInstitucion, DocenteInstitucionUpdateRequest request) {
        Optional<DocenteInstitucion> opt = docenteInstitucionRepo.buscarPorId(idDocenteInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE_INSTITUCION.noEncontrado(), null);
        }

        DocenteInstitucion entidad = opt.get();
        if (request.getIdDocente() != null){
            Optional<Docente> docenteOpt = docenteRepo.buscarPorId(request.getIdDocente());
            if (docenteOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.DOCENTE.noEncontrado(), null);
            }
            entidad.setDocente(docenteOpt.get());
        }
        if (request.getIdInstitucion() != null){
            Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(request.getIdInstitucion());
            if (institucionOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
            }
            entidad.setInstitucion(institucionOpt.get());
        }


        DocenteInstitucion actualizado = docenteInstitucionRepo.save(entidad);

        return new ObjectResponse<>(
                200,
                Modulo.DOCENTE_INSTITUCION.actualizado(),
                mapperService.convDocenteInstitucionAll(actualizado));
    }

    // ========================= ELIMINAR =========================
    @Override
    @Transactional
    public ObjectResponse<String> eliminar(Long idDocenteInstitucion) {
        Optional<DocenteInstitucion> opt = docenteInstitucionRepo.buscarPorId(idDocenteInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.DOCENTE_INSTITUCION.noEncontrado(), null);
        }

        DocenteInstitucion entidad = opt.get();
        entidad.setEnabled(false);
        docenteInstitucionRepo.save(entidad);

        return new ObjectResponse<>(200, Modulo.DOCENTE_INSTITUCION.eliminado(), null);
    }
}
