package com.qt.qtBackend.repository.interfaces;


import com.qt.qtBackend.Enum.TipoDocumentoEnum;
import com.qt.qtBackend.model.Alumno;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAlumnoRepo extends IGenericRepo<Alumno,Long> {
    @Query("SELECT COUNT(e) FROM Alumno e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT e FROM Alumno e WHERE e.enabled = true")
    List<Alumno> listar();

    @Query("SELECT e FROM Alumno e WHERE e.enabled = true")
    Page<Alumno> listarPage(Pageable pageable);

    @Query("SELECT e FROM Alumno e WHERE e.idAlumno = :id AND e.enabled = true")
    Optional<Alumno> buscarPorId(@Param("id") Long idAlumno);

    @Query("SELECT e FROM Alumno e WHERE e.usuario.idUsuario = :id AND e.enabled = true")
    Optional<Alumno> buscarPorIdUsuario(@Param("id") Long idUsuario);

    @Query("SELECT e FROM Alumno e WHERE e.tipoDocumento = :tipoDocumento AND e.docIdentidad = :docIdentidad AND e.enabled = true")
    Optional<Alumno> buscarPorDocumento(@Param("tipoDocumento") TipoDocumentoEnum tipoDocumento,
                                         @Param("docIdentidad") String docIdentidad);
}
