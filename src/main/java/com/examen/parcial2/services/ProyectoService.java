
package com.examen.parcial2.services;

import com.examen.parcial2.models.Proyectos;
import com.examen.parcial2.models.Usuarios;
import com.examen.parcial2.repository.ProyectoRepository;
import com.examen.parcial2.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProyectoService(
            ProyectoRepository proyectoRepository,
            UsuarioRepository usuarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Proyectos> listarPorUsuario(String username) {
        return proyectoRepository.findByUsuario_Username(username);
    }

    @Transactional
    public Proyectos crearProyecto(
            String username,
            String nombre,
            String descripcion) {

        Usuarios usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        long proyectosActivos =
                proyectoRepository.countByUsuario_IdUsuarioAndActivoTrue(
                        usuario.getIdUsuario()
                );

        if (proyectosActivos >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No puedes tener más de 3 proyectos activos"
            );
        }

        Proyectos proyecto = new Proyectos();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setActivo(true);
        proyecto.setUsuario(usuario);

        return proyectoRepository.save(proyecto);
    }
}
