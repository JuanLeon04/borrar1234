package com.humanitas.backend.Controller;

import com.humanitas.backend.entity.Alerta;
import com.humanitas.backend.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping
    public Alerta crearAlerta(@RequestBody Alerta alerta) {
        return alertaService.crearAlerta(alerta);
    }

    @GetMapping
    public List<Alerta> obtenerTodasLasAlertas() {
        return alertaService.obtenerTodasLasAlertas();
    }

    @GetMapping("/{id}")
    public Optional<Alerta> obtenerAlertaPorId(@PathVariable int id) {
        return alertaService.obtenerAlertaPorId(id);
    }

    @PutMapping("/{id}")
    public Alerta actualizarAlerta(@PathVariable int id, @RequestBody Alerta alertaActualizada) {
        return alertaService.actualizarAlerta(id, alertaActualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminarAlerta(@PathVariable int id) {
        alertaService.eliminarAlerta(id);
    }
}
