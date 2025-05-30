package com.humanitas.backend.repository;

import com.humanitas.backend.entity.DiaSemana;
import com.humanitas.backend.entity.MenuSemanal;
import com.humanitas.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuSemanalRepository extends JpaRepository<MenuSemanal, Integer> { // MenuSemanal y el tipo de su ID (int)

    // Métodos de búsqueda personalizados (Spring Data JPA los implementará automáticamente)

    // Buscar el menú para un día específico de la semana
    List<MenuSemanal> findByDia(DiaSemana dia);

    // Buscar un plato específico por su nombre en el menú semanal
    Optional<MenuSemanal> findByNombrePlato(String nombrePlato);

    // Buscar platos que contengan una palabra clave en el nombre
    List<MenuSemanal> findByNombrePlatoContainingIgnoreCase(String palabraClave);

    // Buscar platos disponibles (con disponibilidadPlato > 0)
    List<MenuSemanal> findByDisponibilidadPlatoGreaterThan(int cantidad);

    List<MenuSemanal> findByDiaIn(List<DiaSemana> dias);
}