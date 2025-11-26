package com.qt.qtBackend.service.impl;

import com.qt.qtBackend.Enum.Modulo;
import com.qt.qtBackend.Enum.RolEnum;
import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoAllResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoShortResponse;
import com.qt.qtBackend.mapper.IMapperService;
import com.qt.qtBackend.model.*;
import com.qt.qtBackend.repository.base.IGenericRepo;
import com.qt.qtBackend.repository.interfaces.IInstitucionRepo;
import com.qt.qtBackend.repository.interfaces.IMaterialAcademicoRepo;
import com.qt.qtBackend.repository.interfaces.IUsuarioRepo;
import com.qt.qtBackend.service.base.CRUDImpl;
import com.qt.qtBackend.service.interfaces.IAzureBlobService;
import com.qt.qtBackend.service.interfaces.IMaterialAcademicoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialAcademicoServiceImpl
        extends CRUDImpl<MaterialAcademico, Long>
        implements IMaterialAcademicoService {

    private final IMaterialAcademicoRepo materialAcademicoRepo;
    private final IInstitucionRepo institucionRepo;
    private final IAzureBlobService azureBlobService;
    private final IUsuarioRepo usuarioRepo;
    private final IMapperService mapperService;

    @Override
    protected IGenericRepo<MaterialAcademico, Long> getRepo() {
        return materialAcademicoRepo;
    }

    @Override
    public ObjectResponse<MaterialAcademicoAllResponse> registrarMaterialAcademicoInstitucion(
            Long idInstitucion, MultipartFile file, String descripcion) {

        Optional<Institucion> opt = institucionRepo.buscarPorId(idInstitucion);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null);
        }

        try {
            // validar tipo de archivo
            if (!azureBlobService.isDocument(file)) {
                return new ObjectResponse<>(400,
                        "El archivo debe ser un documento PDF, EXCEL, PPT o WORD",
                        null);
            }

            // validar tamaño maximo 20MB
            if (!azureBlobService.isValidFileSize(file, 20)) {
                return new ObjectResponse<>(400,
                        "El archivo no puede superar los 20MB",
                        null);
            }

            // subir archivo al contenedor
            String url = azureBlobService.uploadMaterialAcademico(file);

            // crear registro
            MaterialAcademico material = MaterialAcademico.builder()
                    .descripcion(descripcion)
                    .url(url)
                    .tipo(azureBlobService.getTipoFile(file))
                    .usuario(opt.get().getUsuario()) // asociar usuario institucional
                    .build();

            materialAcademicoRepo.save(material);

            return new ObjectResponse<>(
                    201,
                    Modulo.MATERIAL_ACADEMICO.registrado(),
                    mapperService.convMaterialAcademicoAll(material)
            );

        } catch (Exception e) {
            log.error("Error al registrar material académico: {}", e.getMessage());
            return new ObjectResponse<>(
                    500,
                    "Error interno al registrar material académico",
                    null
            );
        }
    }
    @Override
    public ObjectResponse<MaterialAcademicoAllResponse> registrarMaterialAcademicoUgel(
            MultipartFile file, String descripcion
    ){
        Optional<Usuario> ugel = usuarioRepo.buscarPorRol(RolEnum.UGEL.getValor());
        if(ugel.isEmpty()){
            return new ObjectResponse<>(404, Modulo.USUARIO.noEncontrado(), null);
        }
        Usuario usuario = ugel.get();
        try {
            // validar tipo de archivo
            if (!azureBlobService.isDocument(file)) {
                return new ObjectResponse<>(400,
                        "El archivo debe ser un documento PDF, EXCEL, PPT o WORD",
                        null);
            }

            // validar tamaño maximo 20MB
            if (!azureBlobService.isValidFileSize(file, 20)) {
                return new ObjectResponse<>(400,
                        "El archivo no puede superar los 20MB",
                        null);
            }

            // subir archivo al contenedor
            String url = azureBlobService.uploadMaterialAcademico(file);

            // crear registro
            MaterialAcademico material = MaterialAcademico.builder()
                    .descripcion(descripcion)
                    .url(url)
                    .tipo(azureBlobService.getTipoFile(file))
                    .usuario(usuario) // asociar usuario institucional
                    .build();

            materialAcademicoRepo.save(material);

            return new ObjectResponse<>(
                    201,
                    Modulo.MATERIAL_ACADEMICO.registrado(),
                    mapperService.convMaterialAcademicoAll(material)
            );

        } catch (Exception e) {
            log.error("Error al registrar material académico: {}", e.getMessage());
            return new ObjectResponse<>(
                    500,
                    "Error interno al registrar material académico",
                    null
            );
        }
    }
    @Override
    public ListResponse<MaterialAcademicoShortResponse> listarMaterialUgel() {
        Optional<Usuario> ugel = usuarioRepo.buscarPorRol(RolEnum.UGEL.getValor());
        if (ugel.isEmpty()) {
            return new ListResponse<>(404, Modulo.USUARIO.noEncontrado(), null, null);
        }

        Long idUgel = ugel.get().getIdUsuario();
        var lista = materialAcademicoRepo.listarPorUsuario(idUgel)
                .stream()
                .map(mapperService::convMaterialAcademicoShort)
                .toList();

        return new ListResponse<>(200, Modulo.MATERIAL_ACADEMICO.listado(), lista, null);
    }

    @Override
    public ListResponse<MaterialAcademicoShortResponse> listarPorInstitucion(Long idInstitucion) {

        // validar institución
        Optional<Institucion> opt = institucionRepo.buscarPorId(idInstitucion);
        if (opt.isEmpty()) {
            return new ListResponse<>(404, Modulo.INSTITUCION.noEncontrado(), null, null);
        }

        Usuario usuario = opt.get().getUsuario();

        var lista = materialAcademicoRepo.listarPorUsuario(usuario.getIdUsuario())
                .stream()
                .map(mapperService::convMaterialAcademicoShort)
                .toList();

        return new ListResponse<>(200, "Material académico del usuario", lista, null);
    }


    @Override
    public ObjectResponse<String> eliminarMaterial(Long idMaterialAcademico) {

        Optional<MaterialAcademico> opt = materialAcademicoRepo.buscarPorId(idMaterialAcademico);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATERIAL_ACADEMICO.noEncontrado(), null);
        }

        try {
            MaterialAcademico material = opt.get();

            // borrar archivo en Azure
            azureBlobService.deleteMaterialAcademico(material.getUrl());

            // marcar como eliminado
            material.setEnabled(false);
            materialAcademicoRepo.save(material);

            return new ObjectResponse<>(200, Modulo.MATERIAL_ACADEMICO.eliminado(), null);

        } catch (Exception e) {
            log.error("Error eliminando material académico: {}", e.getMessage());
            return new ObjectResponse<>(500, "Error interno al eliminar material académico", null);
        }
    }

    @Override
    public ObjectResponse<MaterialAcademicoAllResponse> buscarMaterial(Long idMaterialAcademico) {

        Optional<MaterialAcademico> opt = materialAcademicoRepo.buscarPorId(idMaterialAcademico);
        if (opt.isEmpty()) {
            return new ObjectResponse<>(404, Modulo.MATERIAL_ACADEMICO.noEncontrado(), null);
        }
        return  new ObjectResponse<>(200, Modulo.MATERIAL_ACADEMICO.encontrado(),
                mapperService.convMaterialAcademicoAll(opt.get()));

    }


}
