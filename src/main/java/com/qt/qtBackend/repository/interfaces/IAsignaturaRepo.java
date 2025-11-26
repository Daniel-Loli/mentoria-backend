package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.model.Asignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAsignaturaRepo extends IGenericRepo<Asignatura,Long> {

    @Query("SELECT COUNT(e) FROM Asignatura e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT e FROM Asignatura e WHERE e.enabled = true")
    List<Asignatura> listar();

    @Query("SELECT e FROM Asignatura e WHERE e.enabled = true")
    Page<Asignatura> listarPage(Pageable pageable);

    @Query("SELECT e FROM Asignatura e WHERE e.idAsignatura = :id AND e.enabled = true")
    Optional<Asignatura> buscarPorId(@Param("id") Long idAsignatura);


    @Query("SELECT a FROM Asignatura a WHERE LOWER(a.nombre) = LOWER(:nombre)")
    Optional<Asignatura> buscarPorNombre(@Param("nombre") String nombre);
}
