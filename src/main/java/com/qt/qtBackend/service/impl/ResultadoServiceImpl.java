package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.resultado.ResultadoAllResponse;
import com.qt.qtBackend.dto.resultado.ResultadoCreateRequest;
import com.qt.qtBackend.dto.resultado.ResultadoUpdateRequest;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.model.Matricula;
import com.qt.qtBackend.model.Resultado;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAsignaturaRepo;
import com.qt.qtBackend.repository.interfaces.IMatriculaRepo;
import com.qt.qtBackend.repository.interfaces.IResultadoRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IResultadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultadoServiceImpl
        extends CRUDImpl<Resultado, Long>
        implements IResultadoService {

    private final IResultadoRepo resultadoRepo;
    private final IMatriculaRepo matriculaRepo;
    private final IAsignaturaRepo asignaturaRepo;
    private final IMapperService mapperService;


    @Override
    protected IGenericRepo<Resultado, Long> getRepo() {
        return resultadoRepo;
    }

    @Override
    public ObjectResponse<ResultadoAllResponse> buscar(Long idResultado) {
        Optional<Resultado> opt = resultadoRepo.buscarPorId(idResultado);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.RESULTADO.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.RESULTADO.encontrado(),
                mapperService.convResultadoAll(opt.get()));
    }

    @Override
    public ObjectResponse<ResultadoAllResponse> registrar(ResultadoCreateRequest request) {
        Optional<Matricula> matriculaOpt = matriculaRepo.buscarPorId(request.getIdMatricula());
        if (matriculaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATRICULA.noEncontrado(), null);
        }
        Optional<Asignatura> asignaturaOpt = asignaturaRepo.buscarPorId(request.getIdMatricula());
        if (asignaturaOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNATURA.noEncontrado(), null);
        }
        Resultado resultado = Resultado.builder()
                .matricula(matriculaOpt.get())
                .asignatura(asignaturaOpt.get())
                .nota(request.getNota())
                .periodo(request.getPeriodo())
                .build();
        resultadoRepo.save(resultado);
        return new ObjectResponse<>(201, Modulo.RESULTADO.registrado(),
                mapperService.convResultadoAll(resultado));
    }

    @Override
    public ListResponse<ResultadoAllResponse> registrarAll(List<ResultadoCreateRequest> requests) {
        List<ResultadoAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (ResultadoCreateRequest request : requests) {
            try {
                ObjectResponse<ResultadoAllResponse> response = registrar(request);
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
                Modulo.RESULTADO.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }

    @Override
    public ObjectResponse<ResultadoAllResponse> actualizar(Long idResultado, ResultadoUpdateRequest request) {
        Optional<Resultado> opt = resultadoRepo.buscarPorId(idResultado);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.RESULTADO.noEncontrado(), null);
        }
        Resultado resultado = opt.get();
        resultado.setNota(request.getNota());
        resultadoRepo.save(resultado);
        return new ObjectResponse<>(200, Modulo.RESULTADO.actualizado(),
                mapperService.convResultadoAll(resultado));
    }

    @Override
    public ObjectResponse<String> eliminar(Long idResultado) {
        Optional<Resultado> opt = resultadoRepo.buscarPorId(idResultado);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.RESULTADO.noEncontrado(), null);
        }
        Resultado resultado = opt.get();
        resultado.setEnabled(false);
        resultadoRepo.save(resultado);
        return new ObjectResponse<>(200, Modulo.RESULTADO.eliminado(), null);
    }
}
