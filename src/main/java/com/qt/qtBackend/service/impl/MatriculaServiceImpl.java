package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.PeriodoEnum;
import com.qt.qtBackend.Enum.TipoPeriodoEnum;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.matricula.MatriculaAllResponse;
import com.qt.qtBackend.dto.matricula.MatriculaCreateRequest;
import com.qt.qtBackend.dto.matricula.MatriculaShortResponse;
import com.qt.qtBackend.dto.matricula.MatriculaUpdateRequest;
import com.qt.qtBackend.dto.matricula.MatriculaArbolResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.model.Matricula;
import com.qt.qtBackend.model.Resultado;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAlumnoInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IMatriculaRepo;
import com.qt.qtBackend.repository.interfaces.IResultadoRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IMatriculaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl
        extends CRUDImpl<Matricula, Long>
        implements IMatriculaService {

    private final IMatriculaRepo matriculaRepo;
    private final IAlumnoInstitucionRepo alumnoInstitucionRepo;
    private final IResultadoRepo resultadoRepo;
    private final IMapperService mapperService;
    @Override
    protected IGenericRepo<Matricula, Long> getRepo() {
        return matriculaRepo;
    }

    @Override
    public ObjectResponse<Integer> contarSegunAlumnoInstitucion(Long idAlumnoInstitucion) {
        Integer contar = matriculaRepo.contarSegunAlumnoInstitucion(idAlumnoInstitucion);
        return new ObjectResponse<>(200, Modulo.MATRICULA.contar(), contar);
    }

    @Override
    public ListResponse<MatriculaShortResponse> listarSegunAlumnoInstitucion(Long idAlumnoInstitucion) {
        List<MatriculaShortResponse> lista = matriculaRepo.listarSegunAlumnoInstitucion(idAlumnoInstitucion)
                .stream()
                .sorted(Comparator.comparing(Matricula::getCreatedAt).reversed())
                .map(mapperService::convMatriculaShort)
                .collect(Collectors.toList());
        return new ListResponse<>(200, Modulo.MATRICULA.listado(), lista,lista.size());
    }

    @Override
    public ListPageResponse<MatriculaShortResponse> listarPageSegunAlumnoInstitucion(Pageable pageable, Long idAlumnoInstitucion) {
        var page = matriculaRepo.listarPageSegunAlumnoInstitucion(pageable,idAlumnoInstitucion)
                .map(mapperService::convMatriculaShort);
        return new ListPageResponse<>(
                200,
                Modulo.MATRICULA.listadoPage(),
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ObjectResponse<MatriculaAllResponse> buscar(Long idMatricula) {
        Optional<Matricula> opt = matriculaRepo.buscarPorId(idMatricula);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.MATRICULA.encontrado(),
                mapperService.convMatriculaAll(opt.get()));
    }

    @Override
    public ObjectResponse<MatriculaArbolResponse> obtenerMatriculaResultado(Long idMatricula) {
        Optional<Matricula> matriculaOpt = matriculaRepo.buscarPorId(idMatricula);
        if (matriculaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }

        Matricula matricula = matriculaOpt.get();

        // Obtener todos los resultados de esa matrícula
        List<Resultado> resultados = resultadoRepo.listarPorMatricula(matricula.getIdMatricula());

        // Agrupar resultados por PeriodoEnum
        Map<PeriodoEnum, List<Resultado>> resultadosPorPeriodo = resultados.stream()
                .collect(Collectors.groupingBy(Resultado::getPeriodo));

        // Construir lista de periodos con asignaturas y notas
        List<MatriculaArbolResponse.PeriodoDTO> periodos = resultadosPorPeriodo.entrySet().stream()
                .map(entry -> {
                    List<MatriculaArbolResponse.AsignaturaNotaDTO> asignaturas = entry.getValue().stream()
                            .map(r -> MatriculaArbolResponse.AsignaturaNotaDTO.builder()
                                    .nombreAsignatura(r.getAsignatura().getNombre())
                                    .nota(r.getNota())
                                    .idResultado(r.getIdResultado())
                                    .build())
                            .toList();

                    return MatriculaArbolResponse.PeriodoDTO.builder()
                            .periodo(entry.getKey())
                            .asignaturas(asignaturas)
                            .build();
                })
                .toList();

        MatriculaArbolResponse response = MatriculaArbolResponse.builder()
                .idMatricula(matricula.getIdMatricula())
                .anio(matricula.getAnio())
                .nivel(matricula.getNivel())
                .grado(matricula.getGrado())
                .estado(matricula.getEstado())
                .tipoPeriodo(matricula.getAlumnoInstitucion().getInstitucion().getTipoPeriodo())
                .periodos(periodos)
                .build();

        return new ObjectResponse<>(200, Modulo.MATRICULA.encontrado(), response);
    }


    @Override
    public ObjectResponse<MatriculaAllResponse> registrar(MatriculaCreateRequest request) {
        Optional<AlumnoInstitucion> opt = alumnoInstitucionRepo.buscarPorId(request.getIdAlumnoInstitucion());
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }
        AlumnoInstitucion alumnoInstitucion = opt.get();
        TipoPeriodoEnum tipoPeriodo = alumnoInstitucion.getInstitucion().getTipoPeriodo();
        Matricula matricula = Matricula.builder()
                .alumnoInstitucion(alumnoInstitucion)
                .anio(request.getAnio())
                .nivel(request.getNivel())
                .grado(request.getGrado())
                .estado(request.getEstado())
                .build();
        matriculaRepo.save(matricula);
        return new ObjectResponse<>(201, Modulo.MATRICULA.registrado(),
                mapperService.convMatriculaAll(matricula));
    }

    @Override
    public ListResponse<MatriculaAllResponse> registrarAll(List<MatriculaCreateRequest> requests) {
        List<MatriculaAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (MatriculaCreateRequest request : requests) {
            try {
                ObjectResponse<MatriculaAllResponse> response = registrar(request);
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
                Modulo.MATRICULA.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }
    @Override
    public ObjectResponse<MatriculaAllResponse> actualizar(Long idMatricula, MatriculaUpdateRequest request) {
        Optional<Matricula> opt = matriculaRepo.buscarPorId(idMatricula);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }
        Matricula matricula = opt.get();
        matricula.setEstado(request.getEstado());
        return new ObjectResponse<>(200, Modulo.MATRICULA.actualizado(),
                mapperService.convMatriculaAll(matricula));
    }

    @Override
    public ObjectResponse<String> eliminar(Long idMatricula) {
        Optional<Matricula> opt = matriculaRepo.buscarPorId(idMatricula);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }
        Matricula matricula = opt.get();
        matricula.setEnabled(false);
        matriculaRepo.save(matricula);
        return new ObjectResponse<>(200, Modulo.MATRICULA.eliminado(), null);
    }
}
