package com.humanitas.backend.service;

import com.humanitas.backend.entity.Alerta;
import com.humanitas.backend.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    // Crear una nueva alerta
    public Alerta crearAlerta(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    // Obtener una alerta por su ID
    public Optional<Alerta> obtenerAlertaPorId(Integer id) {
        return alertaRepository.findById(id);
    }

    // Obtener todas las alertas
    public List<Alerta> obtenerTodasLasAlertas() {
        return alertaRepository.findAll();
    }

    // Actualizar una alerta existente
    public Alerta actualizarAlerta(Integer id, Alerta alertaActualizada) {
        if (alertaRepository.existsById(id)) {
            alertaActualizada.setId(id);
            return alertaRepository.save(alertaActualizada);
        } else {
            throw new RuntimeException("Alerta no encontrada");
        }
    }

    // Eliminar una alerta por su ID
    public void eliminarAlerta(Integer id) {
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Alerta no encontrada");
        }
    }
}
