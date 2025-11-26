package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Rol;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IRolRepo extends IGenericRepo<Rol, Long> {
    @Query("SELECT e FROM Rol e")
    List<Rol> listar();

    @Query("SELECT e FROM Rol e WHERE e.idRol = :id")
    Optional<Rol> buscarPorId(@Param("id") Long idRol);
}
