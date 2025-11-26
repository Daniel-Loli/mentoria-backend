package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IInstitucionRepo extends IGenericRepo<Institucion,Long> {

    @Query("SELECT COUNT(e) FROM Institucion e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT e FROM Institucion e WHERE e.enabled = true")
    List<Institucion> listar();

    @Query("SELECT e FROM Institucion e WHERE e.enabled = true")
    Page<Institucion> listarPage(Pageable pageable);

    @Query("SELECT e FROM Institucion e WHERE e.idInstitucion = :id AND e.enabled = true")
    Optional<Institucion> buscarPorId(@Param("id") Long idInstitucion);

    @Query("SELECT e FROM Institucion e WHERE e.usuario.idUsuario = :id AND e.enabled = true")
    Optional<Institucion> buscarPorIdUsuario(@Param("id") Long idUsuario);

}
