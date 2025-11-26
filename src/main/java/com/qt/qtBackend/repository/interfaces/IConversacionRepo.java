package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Conversacion;
import com.qt.qtBackend.model.Docente;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IConversacionRepo extends IGenericRepo<Conversacion,Long> {

    @Query("SELECT e FROM Conversacion e WHERE e.enabled = true AND e.usuario.idUsuario = :idUsuario")
    List<Conversacion> listarSegunUsuario(Long idUsuario);

    @Query("SELECT e FROM Conversacion e WHERE e.enabled = true AND e.usuario.idUsuario = :idUsuario")
    Page<Conversacion> listarPageSegunUsuario(Pageable pageable,Long idUsuario);

    @Query("SELECT e FROM Conversacion e WHERE e.idConversacion = :id AND e.enabled = true")
    Optional<Conversacion> buscarPorId(@Param("id") Long idConversacion);

}
