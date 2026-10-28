package com.examen.parcial2.controller;

import com.examen.parcial2.models.Proyectos;
import com.examen.parcial2.services.ProyectoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponse>> listar(
            Authentication authentication) {

        String username = authentication.getName();

        List<ProyectoResponse> proyectos =
                proyectoService.listarPorUsuario(username)
                        .stream()
                        .map(ProyectoResponse::from)
                        .toList();

        return ResponseEntity.ok(proyectos);
    }

    @PostMapping
    public ResponseEntity<ProyectoResponse> crear(
            @Valid @RequestBody CrearProyectoRequest request,
            Authentication authentication) {

        Proyectos proyecto = proyectoService.crearProyecto(
                authentication.getName(),
                request.nombre(),
                request.descripcion()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProyectoResponse.from(proyecto));
    }

    public record CrearProyectoRequest(
            @NotBlank
            @Size(max = 100)
            String nombre,

            String descripcion
    ) {}

    public record ProyectoResponse(
            Long id,
            String nombre,
            String descripcion,
            Boolean activo
    ) {
        public static ProyectoResponse from(Proyectos proyecto) {
            return new ProyectoResponse(
                    proyecto.getId(),
                    proyecto.getNombre(),
                    proyecto.getDescripcion(),
                    proyecto.getActivo()
            );
        }
    }
}