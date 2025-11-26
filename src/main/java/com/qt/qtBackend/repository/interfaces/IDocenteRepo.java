package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.Enum.TipoDocumentoEnum;
import com.qt.qtBackend.model.Docente;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IDocenteRepo extends IGenericRepo<Docente,Long> {

    @Query("SELECT COUNT(e) FROM Docente e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT e FROM Docente e WHERE e.enabled = true")
    List<Docente> listar();

    @Query("SELECT e FROM Docente e WHERE e.enabled = true")
    Page<Docente> listarPage(Pageable pageable);

    @Query("SELECT e FROM Docente e WHERE e.idDocente = :id AND e.enabled = true")
    Optional<Docente> buscarPorId(@Param("id") Long idDocente);

    @Query("SELECT e FROM Docente e WHERE e.usuario.idUsuario = :id AND e.enabled = true")
    Optional<Docente> buscarPorIdUsuario(@Param("id") Long idUsuario);

    @Query("SELECT e FROM Docente e WHERE e.tipoDocumento = :tipoDocumento AND e.docIdentidad = :docIdentidad AND e.enabled = true")
    Optional<Docente> buscarPorDocumento(@Param("tipoDocumento") TipoDocumentoEnum tipoDocumento,
                                         @Param("docIdentidad") String docIdentidad);
}
