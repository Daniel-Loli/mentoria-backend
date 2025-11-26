package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Matricula;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMatriculaRepo  extends IGenericRepo<Matricula,Long> {
    @Query("SELECT COUNT(e) FROM Matricula e WHERE e.enabled = true AND e.alumnoInstitucion.idAlumnoInstitucion = :idAlumnoInstitucion ")
    Integer contarSegunAlumnoInstitucion(Long idAlumnoInstitucion);

    @Query("SELECT e FROM Matricula e WHERE e.enabled = true AND e.alumnoInstitucion.idAlumnoInstitucion = :idAlumnoInstitucion")
    List<Matricula> listarSegunAlumnoInstitucion(Long idAlumnoInstitucion);

    @Query("SELECT e FROM Matricula e WHERE e.enabled = true AND e.alumnoInstitucion.idAlumnoInstitucion = :idAlumnoInstitucion ")
    Page<Matricula> listarPageSegunAlumnoInstitucion(Pageable pageable, Long idAlumnoInstitucion);

    @Query("SELECT e FROM Matricula e WHERE e.idMatricula = :id AND e.enabled = true")
    Optional<Matricula> buscarPorId(@Param("id") Long idMatricula);

    @Query("""
        SELECT e
        FROM Matricula e
        WHERE e.enabled = true
          AND e.alumnoInstitucion.idAlumnoInstitucion = :idAlumnoInstitucion
        ORDER BY e.anio DESC
    """)
    Optional<Matricula> obtenerMatriculaMasActual(Long idAlumnoInstitucion);

}
