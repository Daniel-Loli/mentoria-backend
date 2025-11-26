package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Asignacion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAsignacionRepo extends IGenericRepo<Asignacion,Long> {
    @Query("SELECT COUNT(e) FROM Asignacion e WHERE e.enabled = true AND e.mision.idMision = :idMision")
    Integer contarSegunMision(Long idMision);

    @Query("SELECT e FROM Asignacion e WHERE e.enabled = true AND e.mision.idMision = :idMision")
    List<Asignacion> listarSegunMision(Long idMision);

    @Query("SELECT e FROM Asignacion e WHERE e.enabled = true And e.mision.idMision = :idMision")
    Page<Asignacion> listarPageSegunMision(Pageable pageable, Long idMision);

    @Query("SELECT e FROM Asignacion e WHERE e.idAsignacion = :id AND e.enabled = true")
    Optional<Asignacion> buscarPorId(@Param("id") Long idAsignacion);


    @Query("""
       SELECT a 
       FROM Asignacion a 
       WHERE a.enabled = true 
         AND a.mision.idMision = :idMision 
         AND a.alumnoInstitucion.idAlumnoInstitucion = :idAlumnoInstitucion
       """)
    List<Asignacion> listarSegunMisionYAlumnoInstitucion(Long idMision, Long idAlumnoInstitucion);

}
