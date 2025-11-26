package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.NivelEnum;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.unidad.*;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.model.Unidad;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IUnidadRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IUnidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnidadServiceImpl
        extends CRUDImpl<Unidad, Long>
        implements IUnidadService {

    private final IUnidadRepo unidadRepo;
    private final IInstitucionRepo institucionRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<Unidad, Long> getRepo() {
        return unidadRepo;
    }

    @Override
    public ObjectResponse<UnidadAllResponse> buscar(Long idUnidad) {
        Optional<Unidad> opt = unidadRepo.buscarPorId(idUnidad);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.UNIDAD.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.UNIDAD.encontrado(),
                mapperService.convUnidadAll(opt.get()));
    }

    @Override
    public ListResponse<NivelUnidadDTO> listarNivelesGrados(Long idInstitucion) {
        List<Unidad> unidades = unidadRepo.listarPorInstitucion(idInstitucion);

        if (unidades.isEmpty()) {
            return new ListResponse<>(404, Modulo.UNIDAD.noEncontrado(),null,null);
        }

        List<NivelUnidadDTO> arbol = unidades.stream()
                .collect(Collectors.groupingBy(Unidad::getNivel))
                .entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e1.getKey().getOrden(), e2.getKey().getOrden()))
                .map(nivelEntry -> {
                    NivelEnum nivel = nivelEntry.getKey();
                    List<GradoUnidadDTO> grados = nivelEntry.getValue().stream()
                            .collect(Collectors.groupingBy(Unidad::getGrado))
                            .entrySet().stream()
                            .sorted((g1, g2) -> g1.getKey().compareTo(g2.getKey()))
                            .map(gradoEntry -> {
                                // Tomamos solo la primera unidad del grado
                                Unidad u = gradoEntry.getValue().get(0);
                                UnidadIdResponse unidadResp = UnidadIdResponse.builder()
                                        .idUnidad(u.getIdUnidad())
                                        .enabled(u.getEnabled())
                                        .build();

                                return GradoUnidadDTO.builder()
                                        .grado(gradoEntry.getKey())
                                        .unidad(unidadResp)
                                        .build();
                            })
                            .toList();

                    return NivelUnidadDTO.builder()
                            .nivel(nivel)
                            .grados(grados)
                            .build();
                })
                .toList();

        return new ListResponse<>(200, Modulo.UNIDAD.encontrado(), arbol, arbol.size());
    }

    @Override
    public ObjectResponse<UnidadAllResponse> registrar(UnidadCreateRequest request) {
        Optional<Institucion> opt = institucionRepo.buscarPorId(request.getIdInstitucion());
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }
        Unidad unidad =  Unidad.builder()
                .nivel(request.getNivel())
                .grado(request.getGrado())
                .institucion(opt.get())
                .build();
        unidadRepo.save(unidad);
        return new ObjectResponse<>(201, Modulo.UNIDAD.registrado(),
                mapperService.convUnidadAll(unidad));
    }

    @Override
    public ListResponse<UnidadAllResponse> registrarAll(List<UnidadCreateRequest> requests) {
        List<UnidadAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (UnidadCreateRequest request : requests) {
            try {
                ObjectResponse<UnidadAllResponse> response = registrar(request);
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
                Modulo.UNIDAD.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }

    @Override
    public ObjectResponse<String> eliminar(Long idUnidad) {
        Optional<Unidad> opt = unidadRepo.buscarPorId(idUnidad);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.UNIDAD.noEncontrado(), null);
        }
        Unidad unidad = opt.get();
        unidad.setEnabled(false);
        unidadRepo.save(unidad);
        return new ObjectResponse<>(200, Modulo.UNIDAD.eliminado(), null);
    }
}
