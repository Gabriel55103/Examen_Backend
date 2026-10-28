package com.examen.parcial2.repository;

import com.examen.parcial2.models.Tareas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tareas, Long> {

    List<Tareas> findByProyectoId(Long proyectoId);
}