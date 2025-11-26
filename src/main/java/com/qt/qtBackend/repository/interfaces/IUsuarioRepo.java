package com.qt.qtBackend.repository.interfaces;

import com.qt.qtBackend.model.Usuario;
import com.qt.qtBackend.repository.base.IGenericRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IUsuarioRepo extends IGenericRepo<Usuario,Long> {

    @Query("SELECT COUNT(e) FROM Usuario e WHERE e.enabled = true")
    Integer contar();

    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.enabled = true")
    Optional<Usuario> buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.rol.idRol = :idRol AND u.enabled = true")
    Optional<Usuario> buscarPorRol(@Param("idRol") Long idRol);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM Usuario u WHERE u.codigo = :codigo AND u.enabled = true")
    boolean existePorCodigo(@Param("codigo") String codigo);

    @Query("SELECT e FROM Usuario e WHERE e.enabled = true")
    List<Usuario> listar();

    @Query("SELECT e FROM Usuario e WHERE e.enabled = true")
    Page<Usuario> listarPage(Pageable pageable);

    @Query("SELECT e FROM Usuario e WHERE e.idUsuario = :id AND e.enabled = true")
    Optional<Usuario> buscarPorId(@Param("id") Long idUsuario);


}
