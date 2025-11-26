package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.*;
import com.qt.qtBackend.dto.asignacion.*;
import com.qt.qtBackend.dto.base.ListPageResponse;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.*;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IAlumnoInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IAlumnoRepo;
import com.qt.qtBackend.repository.interfaces.IAsignacionRepo;
import com.qt.qtBackend.repository.interfaces.IMisionRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IAsignacionService;
import com.qt.qtBackend.service.interfaces.IAzureBlobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsignacionServiceImpl
        extends CRUDImpl<Asignacion, Long>
        implements IAsignacionService {
    private final IAsignacionRepo asignacionRepo;
    private final IMisionRepo misionRepo;
    private final IAlumnoInstitucionRepo alumnoInstitucionRepo;
    private final IMapperService mapperService;
    private final IAzureBlobService azureBlobService;


    @Override
    protected IGenericRepo<Asignacion, Long> getRepo() {
        return asignacionRepo;
    }

    @Override
    public ObjectResponse<Integer> contarSegunMision(Long idMision) {
        Integer contar = asignacionRepo.contarSegunMision(idMision);
        return new ObjectResponse<>(200, Modulo.ASIGNACION.contar(), contar);
    }

    @Override
    public ListResponse<AsignacionFilterAShortResponse> listarSegunMision(Long idMision) {
        List<AsignacionFilterAShortResponse> lista = asignacionRepo.listarSegunMision(idMision)
                .stream()
                .sorted(Comparator.comparing(Asignacion::getCreatedAt).reversed())
                .map(mapperService::convAsignacionFilterAShort)
                .collect(Collectors.toList());
        return new ListResponse<>(200, Modulo.ASIGNACION.listado(), lista,lista.size());
    }

    @Override
    public ListPageResponse<AsignacionFilterAShortResponse> listarPageSegunMision(Pageable pageable, Long idMision) {
        var page = asignacionRepo.listarPageSegunMision(pageable,idMision)
                .map(mapperService::convAsignacionFilterAShort);
        return new ListPageResponse<>(
                200,
                Modulo.ASIGNACION.listadoPage(),
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public ObjectResponse<AsignacionAllResponse> buscar(Long idAsignacion) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }
        return new ObjectResponse<>(200, Modulo.ASIGNACION.encontrado(),
                mapperService.convAsignacionAll(opt.get()));
    }

    @Override
    public ObjectResponse<AsignacionAllResponse> registrar(AsignacionCreateRequest request) {
        Optional<Mision> MisionOpt = misionRepo.buscarPorId(request.getIdMision());
        if (MisionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MISION.noEncontrado(), null);
        }
        Mision mision = MisionOpt.get();
        if(mision.getEstado() != EstadoMisionEnum.CONVOCATORIA){
            return new ObjectResponse<>(400, "Solicitud rechazada, El Mision no esta en estado de CONVOCATORIA", null);
        }

        Optional<AlumnoInstitucion> alumnoInstitucionOpt = alumnoInstitucionRepo.buscarPorId(request.getIdAlumnoInstitucion());
        if (alumnoInstitucionOpt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ALUMNO_INSTITUCION.noEncontrado(), null);
        }

        List<Asignacion> asignacionesExistentes = asignacionRepo
                .listarSegunMisionYAlumnoInstitucion(request.getIdMision(), request.getIdAlumnoInstitucion());
        if (!asignacionesExistentes.isEmpty()) {
            return new ObjectResponse<>(400, "Solicitud rechazada, El alumno ya está asignado a este Mision", null);
        }
        Institucion institucion = mision.getInstitucion();
        if(!institucion.equals(alumnoInstitucionOpt.get().getInstitucion())){
            return new ObjectResponse<>(400, "Solicitud rechazada, El alumno no pertenece a esta Institucion", null);
        }

        Asignacion asignacion = Asignacion.builder()
                .mision(MisionOpt.get())
                .alumnoInstitucion(alumnoInstitucionOpt.get())
                .estado(EstadoAsignacionEnum.SOLICITADO)
                .puntaje(0)
                .isUpdate(false)
                .build();
        asignacionRepo.save(asignacion);
        return new ObjectResponse<>(201,Modulo.ASIGNACION.registrado(),mapperService.convAsignacionAll(asignacion));
    }

    @Override
    public ListResponse<AsignacionAllResponse> registrarAll(List<AsignacionCreateRequest> requests) {
        List<AsignacionAllResponse> registrados = new ArrayList<>();
        int errorCount = 0;

        for (AsignacionCreateRequest request : requests) {
            try {
                ObjectResponse<AsignacionAllResponse> response = registrar(request);
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
                Modulo.ASIGNACION.resumenAllRegistro(registrados.size(), errorCount),
                registrados,
                registrados.size()
        );
    }

    @Override
    public ObjectResponse<AsignacionAllResponse> actualizar(Long idAsignacion, AsignacionUpdateRequest request) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }
        Asignacion asignacion = opt.get();
        if(request.getEstado() !=null){
            asignacion.setEstado(request.getEstado());
        }
        if(request.getRespuesta() !=null){
            asignacion.setRespuesta(request.getRespuesta());
        }
        if(request.getPuntaje() !=null){
            actualizarPuntaje(asignacion,request.getPuntaje());
        }
        asignacionRepo.save(asignacion);
        return new ObjectResponse<>(200, Modulo.ASIGNACION.actualizado(),
                mapperService.convAsignacionAll(asignacion));
    }
    private void actualizarPuntaje(Asignacion asignacion, Integer puntaje){
        AlumnoInstitucion alumnoInstitucion = asignacion.getAlumnoInstitucion();
        if(asignacion.getIsUpdate()){
            //se resta el puntaje actual y luego se suma el nuevo puntaje
            alumnoInstitucion.setPuntajeTotal(alumnoInstitucion.getPuntajeTotal() - asignacion.getPuntaje());
            asignacion.setPuntaje(puntaje);
            alumnoInstitucion.setPuntajeTotal(alumnoInstitucion.getPuntajeTotal() + puntaje);
        } else {
            //primera vez
            asignacion.setPuntaje(puntaje);
            alumnoInstitucion.setPuntajeTotal(alumnoInstitucion.getPuntajeTotal() + puntaje);
            asignacion.setIsUpdate(true);
        }

    }

    @Override
    public ObjectResponse<AsignacionEvidenciaResponse> actualizarEvidencia(Long idAsignacion, MultipartFile file) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }

        Asignacion asignacion = opt.get();

        try {
            // Validar si es imagen (o documento si lo permites)
            if (!azureBlobService.isImage(file)) {
                return new ObjectResponse<>(400, "El archivo debe ser una imagen", null);
            }
            if (!azureBlobService.isValidFileSize(file, 5)) {
                return new ObjectResponse<>(400, "El archivo no debe superar los 5MB", null);
            }

            // Eliminar evidencia anterior si existía
            if (asignacion.getEvidencia() != null && !asignacion.getEvidencia().trim().isEmpty()) {
                azureBlobService.deleteImagenEvidencia(asignacion.getEvidencia());
            }

            // Subir nueva evidencia
            String url = azureBlobService.uploadImagenEvidencia(file);

            // Guardar nueva URL en BD
            asignacion.setEvidencia(url);
            asignacionRepo.save(asignacion);

            return new ObjectResponse<>(
                    200,
                    Modulo.ASIGNACION.actualizado(),
                    mapperService.convAsignacionEvidencia(asignacion)
            );

        } catch (IOException e) {
            log.error("Error al subir archivo de evidencia", e);
            return new ObjectResponse<>(500, "Error interno al subir el archivo", null);
        }
    }

    @Override
    public ObjectResponse<AsignacionEvidenciaResponse> buscarAsignacionEvidencia(Long idAsignacion) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }

        Asignacion asignacion = opt.get();
        return new ObjectResponse<>(
                200,
                Modulo.ASIGNACION.encontrado(),
                mapperService.convAsignacionEvidencia(asignacion)
        );
    }

    @Override
    public ObjectResponse<String> eliminarEvidencia(Long idAsignacion) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }

        Asignacion asignacion = opt.get();

        try {
            // Si tiene evidencia, eliminar del Blob Storage
            if (asignacion.getEvidencia() != null && !asignacion.getEvidencia().trim().isEmpty()) {
                azureBlobService.deleteImagenEvidencia(asignacion.getEvidencia());
            }

            // Limpiar campos en BD
            asignacion.setEvidencia(null);
            asignacionRepo.save(asignacion);

            return new ObjectResponse<>(200, "Evidencia eliminada correctamente", null);

        } catch (Exception e) {
            log.error("Error eliminando evidencia: {}", e.getMessage());
            return new ObjectResponse<>(500, "Error interno al eliminar la evidencia", null);
        }
    }



    @Override
    public ObjectResponse<String> eliminar(Long idAsignacion) {
        Optional<Asignacion> opt = asignacionRepo.buscarPorId(idAsignacion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.ASIGNACION.noEncontrado(), null);
        }
        Asignacion asignacion = opt.get();
        asignacion.setEnabled(false);
        asignacionRepo.save(asignacion);
        return new ObjectResponse<>(200, Modulo.ASIGNACION.eliminado(), null);
    }

}
