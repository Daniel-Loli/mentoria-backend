package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.Enum.EstadoMisionEnum;
import com.qt.qtBackend.model.Mision;
import com.qt.qtBackend.model.Mision;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMisionRepo extends IGenericRepo<Mision,Long> {

    @Query("""
    SELECT COUNT(e)
    FROM Mision e
    WHERE e.enabled = true
      AND e.institucion.idInstitucion = :idInstitucion
      AND (:estado IS NULL OR e.estado = :estado)
    """)
    Integer contarSegunInstitucionEstado(Long idInstitucion, EstadoMisionEnum estado);


    @Query("""
    SELECT e
    FROM Mision e
    WHERE e.enabled = true
      AND e.institucion.idInstitucion = :idInstitucion
      AND (:estado IS NULL OR e.estado = :estado)
    """)
    List<Mision> listarSegunInstitucion(Long idInstitucion, EstadoMisionEnum estado);

    @Query("""
    SELECT e
    FROM Mision e
    WHERE e.enabled = true
      AND e.institucion.idInstitucion = :idInstitucion
      AND (:estado IS NULL OR e.estado = :estado)
    """)
    Page<Mision> listarPageSegunInstitucion(Pageable pageable, Long idInstitucion, EstadoMisionEnum estado);


    @Query("SELECT e FROM Mision e WHERE e.idMision = :id AND e.enabled = true")
    Optional<Mision> buscarPorId(@Param("id") Long idMision);
}
