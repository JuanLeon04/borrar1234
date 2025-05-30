package com.humanitas.backend.repository;

import com.humanitas.backend.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {

    // Buscar todas las alertas activas (disponibles)
    List<Alerta> findByActivoTrue();

    // Buscar alerta por usuario
    List<Alerta> findByUsuarioId(Integer usuarioId);
}
