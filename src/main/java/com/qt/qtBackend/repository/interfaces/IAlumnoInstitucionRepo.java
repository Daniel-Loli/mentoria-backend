package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.model.AlumnoInstitucion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAlumnoInstitucionRepo extends IGenericRepo<AlumnoInstitucion,Long> {
    @Query("SELECT COUNT(e) FROM AlumnoInstitucion e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT COUNT(e) FROM AlumnoInstitucion e WHERE e.enabled = true AND e.alumno.idAlumno = :idAlumno")
    Integer contarSegunAlumno(@Param("idAlumno") Long idAlumno);

    @Query("SELECT COUNT(e) FROM AlumnoInstitucion e WHERE e.enabled = true AND e.institucion.idInstitucion = :idInstitucion")
    Integer contarSegunInstitucion(@Param("idInstitucion") Long idInstitucion);

    @Query("SELECT e FROM AlumnoInstitucion e WHERE e.enabled = true")
    List<AlumnoInstitucion> listar();

    @Query("""
            SELECT e FROM AlumnoInstitucion e
            WHERE e.enabled = true
            AND e.alumno.idAlumno = :idAlumno
            """)
    List<AlumnoInstitucion> listarSegunAlumno(@Param("idAlumno") Long idAlumno);

    @Query("""
            SELECT e FROM AlumnoInstitucion e
            WHERE e.enabled = true
            AND e.institucion.idInstitucion = :idInstitucion
            """)
    List<AlumnoInstitucion> listarSegunInstitucion(@Param("idInstitucion") Long idInstitucion);


    @Query("SELECT e FROM AlumnoInstitucion e WHERE e.enabled = true")
    Page<AlumnoInstitucion> listarPage(Pageable pageable);

    @Query("""
            SELECT e FROM AlumnoInstitucion e
            WHERE e.enabled = true
            AND e.alumno.idAlumno = :idAlumno
            """)
    Page<AlumnoInstitucion> listarPageSegunAlumno(Pageable pageable,@Param("idAlumno") Long idAlumno);

    @Query("""
            SELECT e FROM AlumnoInstitucion e
            WHERE e.enabled = true
            AND e.institucion.idInstitucion = :idInstitucion
            """)
    Page<AlumnoInstitucion> listarPageSegunInstitucion(Pageable pageable,@Param("idInstitucion") Long idInstitucion);


    @Query("SELECT e FROM AlumnoInstitucion e WHERE e.idAlumnoInstitucion = :id AND e.enabled = true")
    Optional<AlumnoInstitucion> buscarPorId(@Param("id") Long idAlumnoInstitucion);

    @Query("SELECT e FROM AlumnoInstitucion e WHERE e.enabled = true AND e.alumno.idAlumno = :idAlumno AND e.institucion.idInstitucion = :idInstitucion")
    Optional<AlumnoInstitucion> buscarPorAlumnoInstitucion(@Param("idAlumno") Long idAlumno, @Param("idInstitucion") Long idInstitucion);
}
