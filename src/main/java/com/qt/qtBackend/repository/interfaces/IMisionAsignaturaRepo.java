package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.MisionAsignatura;
import com.qt.qtBackend.model.Mensaje;
import com.qt.qtBackend.model.MisionAsignatura;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMisionAsignaturaRepo extends IGenericRepo<MisionAsignatura,Long> {
    @Query("SELECT COUNT(e) FROM MisionAsignatura e WHERE e.enabled = true AND e.mision.idMision = :idMision")
    Integer contarSegunMision(@Param("idMision") Long idMision);

    @Query("""
            SELECT e FROM MisionAsignatura e
            WHERE e.enabled = true
            AND e.mision.idMision = :idMision
            """)
    List<MisionAsignatura> listarSegunMision(@Param("idMision") Long idMision);

    @Query("""
            SELECT e FROM MisionAsignatura e
            WHERE e.enabled = true
            AND e.mision.idMision = :idMision
            """)
    Page<MisionAsignatura> listarPageSegunMision(Pageable pageable,@Param("idMision") Long idMision);

    @Query("SELECT e FROM MisionAsignatura e WHERE e.idMisionAsignatura = :id AND e.enabled = true")
    Optional<MisionAsignatura> buscarPorId(@Param("id") Long id);


    @Query("""
        SELECT ea 
        FROM MisionAsignatura ea 
        WHERE ea.mision.idMision = :idMision 
          AND ea.asignatura.idAsignatura = :idAsignatura
          AND ea.enabled = true
    """)
    Optional<MisionAsignatura> buscarPorEAId(
            @Param("idMision") Long idMision,
            @Param("idAsignatura") Long idAsignatura
    );
}
