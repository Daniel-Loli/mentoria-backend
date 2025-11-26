package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionAllResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionShortResponse;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.matricula.MatriculaArbolResponse;
import com.qt.qtBackend.dto.mision.*;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.*;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.*;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IMatriculaService;
import com.qt.qtBackend.service.interfaces.IMisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MisionServiceImpl
        extends CRUDImpl<Mision, Long>
        implements IMisionService {

    private final IMisionRepo misionRepo;
    private final IDocenteInstitucionRepo docenteInstitucionRepo;
    private final IInstitucionRepo institucionRepo;
    private final IMatriculaRepo matriculaRepo;
    private final IMatriculaService matriculaService;
    private final IAlumnoInstitucionRepo alumnoInstitucionRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<Mision, Long> getRepo() {
        return misionRepo;
    }

    @Override
    public ListResponse<MisionIAllResponse> listarSegunAlumnoInstitucion(Long idAlumnoInstitucion) {

        Optional<AlumnoInstitucion> alumnoInstitucionOpt = alumnoInstitucionRepo.buscarPorId(idAlumnoInstitucion);
        AlumnoInstitucion alumnoInstitucion = alumnoInstitucionOpt.get();
        //obtener el primer historial de matricula
        Optional<Matricula> matriculaOpt = matriculaRepo.obtenerMatriculaMasActual(idAlumnoInstitucion);
        Matricula matricula = matriculaOpt.get();
        ObjectResponse<MatriculaArbolResponse> arbol = matriculaService.obtenerMatriculaResultado(matricula.getIdMatricula());

        MatriculaArbolResponse responseArbol = arbol.data();
        ListResponse<MisionIAllResponse> responseMisiones = listarSegunInstitucion(alumnoInstitucion.getInstitucion().getIdInstitucion(),EstadoMisionEnum.CONVOCATORIA);
        AlumnoInstitucionAllResponse responseAlumnoInstitucion = mapperService.convAlumnoInstitucionAll(alumnoInstitucion);

        MisionRecomendacionResponse misionRecomendacion = MisionRecomendacionResponse.builder()
                .matriculaActual(responseArbol)
                .alumnoInstitucion(responseAlumnoInstitucion)
                .misionesConvocatorio(responseMisiones)
                .build();
        //lista vacia
        List<MisionIAllResponse> lista = Collections.emptyList();
        return new ListResponse<>(200, Modulo.MISION.listado(), lista,lista.size());
    }

    @Override
    public ObjectResponse<Integer> contarSegunInstitucion(Long idInstitucion,EstadoMisionEnum estado) {
        Integer contar = misionRepo.contarSegunInstitucionEstado(idInstitucion,estado);
        return new ObjectResponse<>(200, Modulo.MISION.contar(), contar);
    }

    @Override
    public ListResponse<MisionIAllResponse> listarSegunInstitucion(Long idInstitucion,EstadoMisionEnum estado) {
        List<MisionIAllResponse> lista = misionRepo.listarSegunInstitucion(idInstitucion,estado)
                .stream()
                .sorted(Comparator.comparing(Mision::getCreatedAt).reversed())
                .map(mapperService::convMisionIAll)
                .collect(Collectors.toList());
        return new ListResponse<>(200, Modulo.MISION.listado(), lista,lista.size());
    }

    @Override
    public ListPageResponse<MisionIAllResponse> listarPageSegunInstitucion(Pageable pageable,Long idInstitucion,EstadoMisionEnum estado) {
        var page = misionRepo.listarPageSegunInstitucion(pageable,idInstitucion,estado)
                .map(mapperService::convMisionIAll);
        return new ListPageResponse<>(
                200,
                Modulo.MISION.listadoPage(),
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ObjectResponse<MisionAllResponse> buscar(Long idMision) {
        Optional<Mision> misionOpt = misionRepo.buscarPorId(idMision);
        if (misionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.MISION.encontrado(),
                mapperService.convMisionAll(misionOpt.get()));
    }

    @Override
    public ObjectResponse<MisionAllResponse> registrar(MisionCreateRequest request) {
        Optional<Institucion> institucionOpt = institucionRepo.buscarPorId(request.getIdInstitucion());
        if (institucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Institucion institucion = institucionOpt.get();

        Mision mision = Mision.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .titulo(request.getTitulo())
                .estado(EstadoMisionEnum.CONVOCATORIA)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .institucion(institucion)
                .build();

        Mision nuevaMision = misionRepo.save(mision);

        return new ObjectResponse<>(201, Modulo.MISION.registrado(),
                mapperService.convMisionAll(nuevaMision));
    }

    @Override
    public ListResponse<MisionAllResponse> registrarAll(List<MisionCreateRequest> requests) {
        List<MisionAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (MisionCreateRequest request : requests) {
            try {
                ObjectResponse<MisionAllResponse> response = registrar(request);
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
                Modulo.MISION.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }


    @Override
    public ObjectResponse<MisionAllResponse> actualizar(Long idMision, MisionUpdateRequest request) {

        Optional<Mision> misionOpt = misionRepo.buscarPorId(idMision);
        if (misionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
        }
        Mision mision = misionOpt.get();

        if (request.getTitulo() != null) mision.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) mision.setDescripcion(request.getDescripcion());
        if (request.getEstado() != null) mision.setEstado(request.getEstado());
        if (request.getFechaInicio() != null) mision.setFechaInicio(request.getFechaInicio());
        if (request.getFechaFin() != null) mision.setFechaFin(request.getFechaFin());
        if(request.getIdDocenteInstitucion() != null){
            Optional<DocenteInstitucion> docenteInstitucionOpt = docenteInstitucionRepo.buscarPorId(request.getIdDocenteInstitucion());
            if (docenteInstitucionOpt.isEmpty()) {
                return new ObjectResponse<>(404, Modulo.DOCENTE_INSTITUCION.noEncontrado(), null);
            }
            DocenteInstitucion docenteInstitucion = docenteInstitucionOpt.get();
            Institucion institucion = mision.getInstitucion();
            if(!institucion.equals(docenteInstitucion.getInstitucion())){
                return new ObjectResponse<>(404, "El docente no pertenece a esta Institucion", null);
            }
            mision.setDocenteInstitucion(docenteInstitucion);
        }


        Mision misionActualizada = misionRepo.save(mision);

        return new ObjectResponse<>(
                200,
                Modulo.MISION.actualizado(),
                mapperService.convMisionAll(misionActualizada)
        );
    }



    @Override
    public ObjectResponse<String> eliminar(Long idMision) {
        Optional<Mision> misionOpt = misionRepo.buscarPorId(idMision);
        if (misionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
        }

        Mision mision = misionOpt.get();
        mision.setEnabled(false);
        misionRepo.save(mision);

        return new ObjectResponse<>(200, Modulo.MISION.eliminado(), null);
    }
}
