package com.qt.qtBackend.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.qt.qtBackend.Enum.TipoContainer;
import com.qt.qtBackend.Enum.TipoFile;
import com.qt.qtBackend.service.interfaces.IAzureBlobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureBlobServiceImpl implements IAzureBlobService {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container.academico}")
    private String academico;

    @Value("${azure.storage.container.evidencia}")
    private String evidencia;

    /**
     * Sube imagen al container de EVIDENCIA
     */
    @Override
    public String uploadImagenEvidencia(MultipartFile file) throws IOException {
        if (!isImage(file)) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = "evidencia_" + UUID.randomUUID() + extension;

        return uploadFile(file, fileName, TipoContainer.EVIDENCIA);
    }

    /**
     * Sube material académico al container de ACADEMICO
     */
    @Override
    public String uploadMaterialAcademico(MultipartFile file) throws IOException {
        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = "material_" + UUID.randomUUID() + extension;

        return uploadFile(file, fileName, TipoContainer.ACADEMICO);
    }

    /**
     * Sube archivo con nombre personalizado a un container específico
     */
    @Override
    public String uploadFile(MultipartFile file, String nombre, TipoContainer tipoContainer) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String fileName = nombre.endsWith(extension) ? nombre : nombre + extension;
        String nombreContainer = getContainerName(tipoContainer);

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(nombreContainer);
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType());

        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);

        String url = blobClient.getBlobUrl();
        log.info("Archivo subido exitosamente: {}", url);
        return url;
    }

    /**
     * Elimina archivo de Azure Blob Storage
     */
    @Override
    public void deleteFile(String fileUrl, TipoContainer tipoContainer) {
        try {
            String blobName = extractBlobNameFromUrl(fileUrl, tipoContainer);
            String nombreContainer = getContainerName(tipoContainer);

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(nombreContainer);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            if (blobClient.exists()) {
                blobClient.delete();
                log.info("Archivo eliminado exitosamente: {}", fileUrl);
            } else {
                log.warn("El archivo no existe en Azure Blob: {}", fileUrl);
            }
        } catch (Exception e) {
            log.error("Error al eliminar archivo: {}", fileUrl, e);
            throw new RuntimeException("Error al eliminar archivo de Azure Blob", e);
        }
    }

    /**
     * Elimina imagen de evidencia
     */
    @Override
    public boolean deleteImagenEvidencia(String fileUrl) {
        try {
            deleteFile(fileUrl, TipoContainer.EVIDENCIA);
            return true;
        } catch (Exception e) {
            log.error("Error eliminando imagen de evidencia: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Elimina material académico
     */
    @Override
    public boolean deleteMaterialAcademico(String fileUrl) {
        try {
            deleteFile(fileUrl, TipoContainer.ACADEMICO);
            return true;
        } catch (Exception e) {
            log.error("Error eliminando material académico: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Reemplaza imagen de evidencia
     */
    @Override
    public String replaceImagenEvidencia(String oldFileUrl, MultipartFile newFile) throws IOException {
        deleteImagenEvidencia(oldFileUrl);
        return uploadImagenEvidencia(newFile);
    }

    /**
     * Reemplaza material académico
     */
    @Override
    public String replaceMaterialAcademico(String oldFileUrl, MultipartFile newFile) throws IOException {
        deleteMaterialAcademico(oldFileUrl);
        return uploadMaterialAcademico(newFile);
    }

    /**
     * Extrae el nombre del blob desde la URL
     */
    private String extractBlobNameFromUrl(String url, TipoContainer tipoContainer) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String nombreContainer = getContainerName(tipoContainer);

            if (path.startsWith("/" + nombreContainer + "/")) {
                return path.substring(("/" + nombreContainer + "/").length());
            } else {
                throw new IllegalArgumentException("URL no corresponde al contenedor configurado");
            }
        } catch (URISyntaxException e) {
            log.error("URL inválida: {}", url, e);
            throw new IllegalArgumentException("URL inválida: " + url, e);
        }
    }

    /**
     * Obtiene el nombre del container según el tipo
     */
    private String getContainerName(TipoContainer tipoContainer) {
        switch (tipoContainer) {
            case ACADEMICO:
                return academico;
            case EVIDENCIA:
                return evidencia;
            default:
                return evidencia;
        }
    }

    @Override
    public boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public boolean isDocument(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.contains("pdf") ||
                        contentType.contains("word") ||
                        contentType.contains("excel") ||
                        contentType.contains("presentation")
        );
    }

    @Override
    public String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    @Override
    public boolean isValidFileSize(MultipartFile file, int maxSizeMB) {
        long maxBytes = maxSizeMB * 1024L * 1024L;
        return file.getSize() <= maxBytes;
    }

    @Override
    public boolean validarRelacionAspecto(MultipartFile file, int ratioWidth, int ratioHeight) {
        try {
            var img = javax.imageio.ImageIO.read(file.getInputStream());
            if (img == null) return false;

            int width = img.getWidth();
            int height = img.getHeight();

            return (width * ratioHeight) == (height * ratioWidth);
        } catch (IOException e) {
            log.error("Error leyendo imagen para validar relación de aspecto", e);
            return false;
        }
    }

    @Override
    public TipoFile getTipoFile(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null) return TipoFile.DEFAULT;

        if (contentType.startsWith("image/")) return TipoFile.IMAGEN;
        if (contentType.contains("pdf")) return TipoFile.PDF;
        if (contentType.contains("word")) return TipoFile.WORD;
        if (contentType.contains("excel")) return TipoFile.EXCEL;
        if (contentType.contains("presentation")) return TipoFile.PPT;

        return TipoFile.DEFAULT;
    }
}