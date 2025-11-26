package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Mensaje;
import com.qt.qtBackend.model.Mensaje;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMensajeRepo extends IGenericRepo<Mensaje,Long> {
    @Query("SELECT e FROM Mensaje e WHERE e.enabled = true AND e.conversacion.idConversacion = :id")
    List<Mensaje> listarSegunConversacion(@Param("id") Long idConversacion);

    @Query("SELECT e FROM Mensaje e WHERE e.enabled = true AND e.conversacion.idConversacion = :id")
    Page<Mensaje> listarPageSegunConversacion(Pageable pageable,@Param("id") Long idConversacion);

    @Query("SELECT e FROM Mensaje e WHERE e.idMensaje = :id AND e.enabled = true")
    Optional<Mensaje> buscarPorId(@Param("id") Long idMensaje);
}
