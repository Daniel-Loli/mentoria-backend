package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.DocenteInstitucion;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDocenteInstitucionRepo extends IGenericRepo<DocenteInstitucion,Long> {

    @Query("SELECT COUNT(e) FROM DocenteInstitucion e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT COUNT(e) FROM DocenteInstitucion e WHERE e.enabled = true AND e.docente.idDocente = :idDocente")
    Integer contarSegunDocente(@Param("idDocente") Long idDocente);

    @Query("SELECT COUNT(e) FROM DocenteInstitucion e WHERE e.enabled = true AND e.institucion.idInstitucion = :idInstitucion")
    Integer contarSegunInstitucion(@Param("idInstitucion") Long idInstitucion);

    @Query("SELECT e FROM DocenteInstitucion e WHERE e.enabled = true")
    List<DocenteInstitucion> listar();

    @Query("""
            SELECT e FROM DocenteInstitucion e
            WHERE e.enabled = true
            AND e.docente.idDocente = :idDocente
            """)
    List<DocenteInstitucion> listarSegunDocente(@Param("idDocente") Long idDocente);

    @Query("""
            SELECT e FROM DocenteInstitucion e
            WHERE e.enabled = true
            AND e.institucion.idInstitucion = :idInstitucion
            """)
    List<DocenteInstitucion> listarSegunInstitucion(@Param("idInstitucion") Long idInstitucion);


    @Query("SELECT e FROM DocenteInstitucion e WHERE e.enabled = true")
    Page<DocenteInstitucion> listarPage(Pageable pageable);

    @Query("""
            SELECT e FROM DocenteInstitucion e
            WHERE e.enabled = true
            AND e.docente.idDocente = :idDocente
            """)
    Page<DocenteInstitucion> listarPageSegunDocente(Pageable pageable,@Param("idDocente") Long idDocente);

    @Query("""
            SELECT e FROM DocenteInstitucion e
            WHERE e.enabled = true
            AND e.institucion.idInstitucion = :idInstitucion
            """)
    Page<DocenteInstitucion> listarPageSegunInstitucion(Pageable pageable,@Param("idInstitucion") Long idInstitucion);


    @Query("SELECT e FROM DocenteInstitucion e WHERE e.idDocenteInstitucion = :id AND e.enabled = true")
    Optional<DocenteInstitucion> buscarPorId(@Param("id") Long idDocenteInstitucion);
}
