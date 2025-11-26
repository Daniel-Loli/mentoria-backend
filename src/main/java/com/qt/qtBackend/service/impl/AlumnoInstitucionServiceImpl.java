package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.EstadoAlumnoInstitucion;
import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.alumnoInstitucion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.*;
import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAlumnoInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IAlumnoRepo;
import com.qt.qtBackend.repository.interfaces.IInstitucionRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IAlumnoInstitucionService;
import com.qt.qtBackend.service.interfaces.IAlumnoInstitucionService;
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
public class AlumnoInstitucionServiceImpl
        extends CRUDImpl<AlumnoInstitucion, Long>
        implements IAlumnoInstitucionService {

    private final IAlumnoInstitucionRepo AlumnoInstitucionRepo;
    private final IAlumnoRepo AlumnoRepo;
    private final IInstitucionRepo institucionRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<AlumnoInstitucion, Long> getRepo() {
        return AlumnoInstitucionRepo;
    }

    // ========================= CONTAR =========================
    @Override
    public ObjectResponse<Integer> contar() {
        Integer total = AlumnoInstitucionRepo.contar();
        return new ObjectResponse<>(200, Modulo.ALUMNO_INSTITUCION.contar(), total);
    }

    @Override
    public ObjectResponse<Integer> contarSegunAlumno(Long idAlumno) {
        Optional<Alumno> AlumnoPpt = AlumnoRepo.buscarPorId(idAlumno);
        if (AlumnoPpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
        }
        Integer total = AlumnoInstitucionRepo.contarSegunAlumno(idAlumno);
        return new ObjectResponse<>(200, Modulo.ALUMNO_INSTITUCION.contar(), total);
    }

    @Override
    public ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Integer total = AlumnoInstitucionRepo.contarSegunInstitucion(idInstitucion);
        return new ObjectResponse<>(200, Modulo.ALUMNO_INSTITUCION.contar(), total);
    }


    // ========================= LISTAR =========================
    @Override
    public ListResponse<AlumnoInstitucionAllResponse> listar() {
        List<AlumnoInstitucionAllResponse> lista = AlumnoInstitucionRepo.listar()
                .stream()
                .sorted(Comparator.comparing(AlumnoInstitucion::getCreatedAt).reversed())
                .map(mapperService::convAlumnoInstitucionAll)
                .toList();

        return new ListResponse<>(200, Modulo.ALUMNO_INSTITUCION.listado(), lista,lista.size());
    }

    @Override
    public ListResponse<AInstitucionAllResponse> listarSegunAlumno(Long idAlumno) {
        Optional<Alumno> AlumnoPpt = AlumnoRepo.buscarPorId(idAlumno);
        if (AlumnoPpt.isEmpty()) {
            return new ListResponse<>(404, Modulo.ALUMNO.noEncontrado(), null,null);
        }
        List<AInstitucionAllResponse> lista = AlumnoInstitucionRepo.listarSegunAlumno(idAlumno)
                .stream()
                .sorted(Comparator.comparing(AlumnoInstitucion::getCreatedAt).reversed())
                .map(mapperService::convAInstitucionAll)
                .toList();

        return new ListResponse<>(200, Modulo.ALUMNO_INSTITUCION.listado(), lista,lista.size());
    }

    @Override
    public ListResponse<AlumnoIAllResponse> listarSegunInstitucion(Long idInstitucion) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(idInstitucion);
        if (institucionOpt.isEmpty()) {
            return new ListResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null,null);
        }
        List<AlumnoIAllResponse> lista = AlumnoInstitucionRepo.listarSegunInstitucion(idInstitucion)
                .stream()
                .sorted(Comparator.comparing(AlumnoInstitucion::getCreatedAt).reversed())
                .map(mapperService::convAlumnoIAll)
                .toList();

        return new ListResponse<>(200, Modulo.ALUMNO_INSTITUCION.listado(), lista,lista.size());
    }

    // ========================= LISTAR PAGINADO =========================
    @Override
    public ListPageResponse<AlumnoInstitucionAllResponse> listarPage(Pageable pageable) {
        Page<AlumnoInstitucion> page = AlumnoInstitucionRepo.listarPage(pageable);

        List<AlumnoInstitucionAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convAlumnoInstitucionAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.listadoPage(),
                data,
                page.getNumber(),         // Página actual
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Indica si es la última página
        );
    }

    @Override
    public ListPageResponse<AInstitucionAllResponse> listarPageSegunAlumno(Pageable pageable, Long idAlumno) {
        Page<AlumnoInstitucion> page = AlumnoInstitucionRepo.listarPageSegunAlumno(pageable, idAlumno);

        List<AInstitucionAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convAInstitucionAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.listadoPage(),
                data,
                page.getNumber(),         // Página actual
                page.getSize(),           // Tamaño de página
                page.getTotalElements(),  // Total de registros
                page.getTotalPages(),     // Total de páginas
                page.isLast()             // Es la última página
        );
    }

    @Override
    public ListPageResponse<AlumnoIAllResponse> listarPageSegunInstitucion(Pageable pageable, Long idInstitucion) {
        Page<AlumnoInstitucion> page = AlumnoInstitucionRepo.listarPageSegunInstitucion(pageable, idInstitucion);

        List<AlumnoIAllResponse> data = page.getContent()
                .stream()
                .map(mapperService::convAlumnoIAll)
                .toList();

        return new ListPageResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.listadoPage(),
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
    public ObjectResponse<AlumnoInstitucionAllResponse> buscar(Long idAlumnoInstitucion) {
        Optional<AlumnoInstitucion> opt = AlumnoInstitucionRepo.buscarPorId(idAlumnoInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
        }

        return new ObjectResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.encontrado(),
                mapperService.convAlumnoInstitucionAll(opt.get()));
    }

    @Override
    public ObjectResponse<AlumnoInstitucionAllResponse> buscarSegunAlumnoInstitucion(Long idAlumno,Long idInstitucion) {
        Optional<AlumnoInstitucion> opt = AlumnoInstitucionRepo.buscarPorAlumnoInstitucion(idAlumno,idInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
        }

        return new ObjectResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.encontrado(),
                mapperService.convAlumnoInstitucionAll(opt.get()));
    }

    // ========================= REGISTRAR =========================
    @Override
    @Transactional
    public ObjectResponse<AlumnoInstitucionAllResponse> registrar(AlumnoInstitucionCreateRequest request) {
        Optional<Alumno> AlumnoPpt = AlumnoRepo.buscarPorId(request.getIdAlumno());
        if (AlumnoPpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
        }
        Alumno Alumno = AlumnoPpt.get();


        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(request.getIdInstitucion());
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Institucion institucion = institucionOpt.get();

        AlumnoInstitucion nuevo = AlumnoInstitucion.builder()
                .alumno(Alumno)
                .institucion(institucion)
                .estado(EstadoAlumnoInstitucion.ACTIVO)
                .puntajeTotal(0)
                .build();

        AlumnoInstitucion guardado = AlumnoInstitucionRepo.save(nuevo);
        return new ObjectResponse<>(
                201,
                Modulo.ALUMNO_INSTITUCION.registrado(),
                mapperService.convAlumnoInstitucionAll(guardado)
        );
    }
    @Override
    public ListResponse<AlumnoInstitucionAllResponse> registrarAll(List<AlumnoInstitucionCreateRequest> requests) {
        List<AlumnoInstitucionAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (AlumnoInstitucionCreateRequest request : requests) {
            try {
                ObjectResponse<AlumnoInstitucionAllResponse> response = registrar(request);
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
                Modulo.ALUMNO_INSTITUCION.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }


    // ========================= ACTUALIZAR =========================
    @Override
    @Transactional
    public ObjectResponse<AlumnoInstitucionAllResponse> actualizar(Long idAlumnoInstitucion, AlumnoInstitucionUpdateRequest request) {
        Optional<AlumnoInstitucion> opt = AlumnoInstitucionRepo.buscarPorId(idAlumnoInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO_INSTITUCION.noEncontrado(), null);
        }

        AlumnoInstitucion entidad = opt.get();
        if (request.getIdAlumno() != null){
            Optional<Alumno> AlumnoOpt = AlumnoRepo.buscarPorId(request.getIdAlumno());
            if (AlumnoOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.ALUMNO.noEncontrado(), null);
            }
            entidad.setAlumno(AlumnoOpt.get());
        }
        if (request.getIdInstitucion() != null){
            Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(request.getIdInstitucion());
            if (institucionOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
            }
            entidad.setInstitucion(institucionOpt.get());
        }


        AlumnoInstitucion actualizado = AlumnoInstitucionRepo.save(entidad);

        return new ObjectResponse<>(
                200,
                Modulo.ALUMNO_INSTITUCION.actualizado(),
                mapperService.convAlumnoInstitucionAll(actualizado));
    }

    // ========================= ELIMINAR =========================
    @Override
    @Transactional
    public ObjectResponse<String> eliminar(Long idAlumnoInstitucion) {
        Optional<AlumnoInstitucion> opt = AlumnoInstitucionRepo.buscarPorId(idAlumnoInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO_INSTITUCION.noEncontrado(), null);
        }

        AlumnoInstitucion entidad = opt.get();
        entidad.setEnabled(false);
        AlumnoInstitucionRepo.save(entidad);

        return new ObjectResponse<>(200, Modulo.ALUMNO_INSTITUCION.eliminado(), null);
    }
}

