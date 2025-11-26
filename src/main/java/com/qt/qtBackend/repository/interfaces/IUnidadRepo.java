package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Unidad;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUnidadRepo extends IGenericRepo<Unidad,Long> {
    @Query("SELECT e FROM Unidad e WHERE e.enabled = true AND e.institucion.idInstitucion = :idInstitucion ")
    List<Unidad> listarPorInstitucion(Long idInstitucion);

    @Query("SELECT e FROM Unidad e WHERE e.enabled = true AND e.idUnidad= :id")
    Optional<Unidad> buscarPorId(@Param("id") Long idUnidad);
}
