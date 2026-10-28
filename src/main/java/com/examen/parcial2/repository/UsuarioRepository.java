
package com.examen.parcial2.repository;

import com.examen.parcial2.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByUsername(String username);

    boolean existsByUsername(String username);
}
