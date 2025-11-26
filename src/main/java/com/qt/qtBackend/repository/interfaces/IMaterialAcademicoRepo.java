package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Institucion;
import com.qt.qtBackend.model.MaterialAcademico;
import com.qt.qtBackend.model.Unidad;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMaterialAcademicoRepo extends IGenericRepo<MaterialAcademico,Long> {

    @Query("SELECT e FROM MaterialAcademico e WHERE e.enabled = true AND e.usuario.idUsuario = :idUsuario ")
    List<MaterialAcademico> listarPorUsuario(Long idUsuario);

    @Query("SELECT e FROM MaterialAcademico e WHERE e.idMaterialAcademico = :id AND e.enabled = true")
    Optional<MaterialAcademico> buscarPorId(@Param("id") Long idMaterialAcademico);
}
