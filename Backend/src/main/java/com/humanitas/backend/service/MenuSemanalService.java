package com.humanitas.backend.service;

import com.humanitas.backend.entity.DiaSemana;
import com.humanitas.backend.entity.MenuSemanal;
import com.humanitas.backend.repository.MenuSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuSemanalService {

    @Autowired
    private MenuSemanalRepository menuSemanalRepository;

    // Crear un nuevo ítem en el menú semanal
    // Solo los administradores deberían poder llamar a este método.
    @Transactional
    public MenuSemanal crearItemMenu(MenuSemanal menuItem) {
        // Validación: Verificar si ya existe un plato con el mismo nombre para el mismo día
        Optional<MenuSemanal> existente = menuSemanalRepository.findByNombrePlato(menuItem.getNombrePlato())
                .filter(m -> m.getDia() == menuItem.getDia()); // Filtra por el mismo día

        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un plato con el nombre '" + menuItem.getNombrePlato() + "' para el día " + menuItem.getDia());
        }
        // Aquí se podrían añadir más validaciones, como que la disponibilidad no sea negativa.
        if (menuItem.getDisponibilidadPlato() < 0) {
            menuItem.setDisponibilidadPlato(0); // O lanzar error
        }
        return menuSemanalRepository.save(menuItem);
    }

    @Transactional
    public void reiniciarMenuLaboral(String horaLimiteCancelacion) {
        List<MenuSemanal> menu = menuSemanalRepository.findByDiaIn(
                List.of(DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES, DiaSemana.VIERNES)
        );

        for (MenuSemanal existente : menu) {
            existente.setNombrePlato("");
            existente.setDescripcionPlato("");
            existente.setImagenPlato(null);
            existente.setDisponibilidadPlato(0);
            existente.setPrecio(0.0);
            existente.setHoraLimiteCancelacion(horaLimiteCancelacion);
        }

        menuSemanalRepository.saveAll(menu); // Guarda los cambios en lote
    }



    // Obtener un ítem del menú por su ID
    @Transactional(readOnly = true)
    public Optional<MenuSemanal> obtenerItemMenuPorId(int id) {
        return menuSemanalRepository.findById(id);
    }

    // Obtener todos los ítems del menú semanal
    @Transactional(readOnly = true)
    public List<MenuSemanal> obtenerTodoElMenu() {
        return menuSemanalRepository.findAll();
    }

    // Obtener el menú para un día específico
    @Transactional(readOnly = true)
    public List<MenuSemanal> obtenerMenuPorDia(DiaSemana dia) {
        return menuSemanalRepository.findByDia(dia);
    }

    // Buscar ítems del menú por nombre (flexible)
    @Transactional(readOnly = true)
    public List<MenuSemanal> buscarItemsMenuPorNombre(String nombre) {
        return menuSemanalRepository.findByNombrePlatoContainingIgnoreCase(nombre);
    }

    // Buscar ítems del menú con disponibilidad
    @Transactional(readOnly = true)
    public List<MenuSemanal> obtenerItemsMenuDisponibles() {
        return menuSemanalRepository.findByDisponibilidadPlatoGreaterThan(0);
    }


    // Actualizar un ítem existente del menú semanal
    // Solo los administradores deberían poder llamar a este método.
    @Transactional
    public MenuSemanal actualizarItemMenu(int id, MenuSemanal menuActualizado) {
        MenuSemanal itemExistente = menuSemanalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ítem del menú no encontrado con ID: " + id));

        // Validación: si el nombre del plato cambia, verificar que no colisione con otro para el mismo día
        if (!itemExistente.getNombrePlato().equals(menuActualizado.getNombrePlato()) ||
                itemExistente.getDia() != menuActualizado.getDia()) {
            Optional<MenuSemanal> colision = menuSemanalRepository.findByNombrePlato(menuActualizado.getNombrePlato())
                    .filter(m -> m.getDia() == menuActualizado.getDia() && m.getId() != id);
            if (colision.isPresent()) {
                throw new RuntimeException("Ya existe otro ítem con el nombre '" + menuActualizado.getNombrePlato() + "' para el día " + menuActualizado.getDia());
            }
        }

        if (menuActualizado.getDisponibilidadPlato() < 0) {
            menuActualizado.setDisponibilidadPlato(0); // O lanzar error
        }

        itemExistente.setDia(menuActualizado.getDia());
        itemExistente.setNombrePlato(menuActualizado.getNombrePlato());
        itemExistente.setDescripcionPlato(menuActualizado.getDescripcionPlato());
        itemExistente.setImagenPlato(menuActualizado.getImagenPlato());
        itemExistente.setDisponibilidadPlato(menuActualizado.getDisponibilidadPlato());
        itemExistente.setPrecio(menuActualizado.getPrecio());
        itemExistente.setHoraLimiteCancelacion(menuActualizado.getHoraLimiteCancelacion());


        return menuSemanalRepository.save(itemExistente);
    }

    // Eliminar un ítem del menú semanal por su ID
    // Solo los administradores deberían poder llamar a este método.
    @Transactional
    public void eliminarItemMenu(int id) {
        if (!menuSemanalRepository.existsById(id)) {
            throw new RuntimeException("Ítem del menú no encontrado con ID: " + id + " para eliminar.");
        }

        menuSemanalRepository.deleteById(id);
    }
}