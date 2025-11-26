package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.UnidadAsignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUnidadAsignaturaRepo extends IGenericRepo<UnidadAsignatura,Long> {
    @Query("SELECT e FROM UnidadAsignatura e WHERE e.enabled = true AND e.unidad.idUnidad = :idUnidad")
    List<UnidadAsignatura> listarPorUnidad(Long idUnidad);

    @Query("SELECT e FROM UnidadAsignatura e WHERE e.enabled = true AND e.idUnidadAsignatura = :id")
    Optional<UnidadAsignatura> buscarPorId(@Param("id") Long idUnidadAsignatura);
}
