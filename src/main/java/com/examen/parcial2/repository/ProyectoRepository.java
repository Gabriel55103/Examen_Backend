
package com.examen.parcial2.repository;

import com.examen.parcial2.models.Proyectos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoRepository
        extends JpaRepository<Proyectos, Long> {

    long countByUsuario_IdUsuarioAndActivoTrue(Long idUsuario);

    boolean existsByIdAndUsuario_IdUsuario(
            Long proyectoId,
            Long idUsuario
    );

    List<Proyectos> findByUsuario_Username(String username);
}
