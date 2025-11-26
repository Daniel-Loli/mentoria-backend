package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.dto.base.ListResponse;
import com.qt.qtBackend.dto.base.ObjectResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoAllResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoShortResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IMaterialAcademicoService {
    ObjectResponse<MaterialAcademicoAllResponse> registrarMaterialAcademicoInstitucion(
            Long idInstitucion, MultipartFile file, String descripcion);
    ObjectResponse<MaterialAcademicoAllResponse> registrarMaterialAcademicoUgel(
            MultipartFile file, String descripcion);
    ListResponse<MaterialAcademicoShortResponse> listarMaterialUgel();
    ListResponse<MaterialAcademicoShortResponse> listarPorInstitucion(Long idInstitucion);
    ObjectResponse<String> eliminarMaterial(Long idMaterialAcademico);
    ObjectResponse<MaterialAcademicoAllResponse> buscarMaterial(Long idMaterialAcademico);

}
