
package com.examen.parcial2.controller;

import com.examen.parcial2.models.Tareas;
import com.examen.parcial2.models.enums.Estado;
import com.examen.parcial2.services.TareaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    public ResponseEntity<TareaResponse> crear(
            @Valid @RequestBody CrearTareaRequest request,
            Authentication authentication) {

        Tareas tarea = tareaService.crearTarea(
                authentication.getName(),
                request.titulo(),
                request.proyectoId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TareaResponse.from(tarea));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoRequest request,
            Authentication authentication) {

        Tareas tarea = tareaService.actualizarEstado(
                id,
                request.estado(),
                authentication.getName()
        );

        return ResponseEntity.ok(TareaResponse.from(tarea));
    }

    public record CrearTareaRequest(
            @NotBlank
            @Size(max = 100)
            String titulo,

            @NotNull
            Long proyectoId
    ) {}

    public record ActualizarEstadoRequest(
            @NotNull
            Estado estado
    ) {}

    public record TareaResponse(
            Long id,
            String titulo,
            Estado estado,
            Long proyectoId
    ) {
        public static TareaResponse from(Tareas tarea) {
            return new TareaResponse(
                    tarea.getId(),
                    tarea.getTitulo(),
                    tarea.getEstado(),
                    tarea.getProyecto().getId()
            );
        }
    }
}
