package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Mision;
import com.qt.qtBackend.model.Resultado;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IResultadoRepo extends IGenericRepo<Resultado,Long>  {


    @Query("SELECT e FROM Resultado e WHERE e.enabled = true AND e.matricula.idMatricula = :idMatricula ")
    List<Resultado> listarPorMatricula(Long idMatricula);

    @Query("SELECT e FROM Resultado e WHERE e.idResultado = :id AND e.enabled = true")
    Optional<Resultado> buscarPorId(@Param("id") Long idResultado);
}
