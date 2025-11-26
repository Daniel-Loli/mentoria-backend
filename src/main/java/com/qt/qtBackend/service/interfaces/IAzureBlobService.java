package com.qt.qtBackend.service.interfaces;

import com.qt.qtBackend.Enum.TipoContainer;
import com.qt.qtBackend.Enum.TipoFile;
import com.qt.qtBackend.Enum.TipoMediaFileEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IAzureBlobService {

    // Métodos específicos para EVIDENCIA (imágenes)
    String uploadImagenEvidencia(MultipartFile file) throws IOException;
    boolean deleteImagenEvidencia(String fileUrl);
    String replaceImagenEvidencia(String oldFileUrl, MultipartFile newFile) throws IOException;

    // Métodos específicos para ACADEMICO (material académico)
    String uploadMaterialAcademico(MultipartFile file) throws IOException;
    boolean deleteMaterialAcademico(String fileUrl);
    String replaceMaterialAcademico(String oldFileUrl, MultipartFile newFile) throws IOException;

    // Métodos genéricos
    String uploadFile(MultipartFile file, String nombre, TipoContainer tipoContainer) throws IOException;
    void deleteFile(String fileUrl, TipoContainer tipoContainer);

    // Métodos de validación
    boolean isImage(MultipartFile file);
    boolean isDocument(MultipartFile file);
    String getFileExtension(String filename);
    boolean isValidFileSize(MultipartFile file, int maxSizeMB);
    boolean validarRelacionAspecto(MultipartFile file, int ratioWidth, int ratioHeight);
    TipoFile getTipoFile(MultipartFile file);
}
