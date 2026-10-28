
package com.examen.parcial2.services;

import com.examen.parcial2.models.Proyectos;
import com.examen.parcial2.models.Tareas;
import com.examen.parcial2.models.enums.Estado;
import com.examen.parcial2.repository.ProyectoRepository;
import com.examen.parcial2.repository.TareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;

    public TareaService(
            TareaRepository tareaRepository,
            ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional
    public Tareas crearTarea(
            String username,
            String titulo,
            Long proyectoId) {

        Proyectos proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proyecto no encontrado"
                ));

        if (!proyecto.getUsuario().getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para crear tareas en este proyecto"
            );
        }

        Tareas tarea = new Tareas();
        tarea.setTitulo(titulo);
        tarea.setEstado(Estado.PENDIENTE);
        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

    @Transactional
    public Tareas actualizarEstado(
            Long tareaId,
            Estado estado,
            String username) {

        Tareas tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarea no encontrada"
                ));

        if (!tarea.getProyecto().getUsuario()
                .getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para modificar esta tarea"
            );
        }

        tarea.setEstado(estado);

        return tareaRepository.save(tarea);
    }
}
