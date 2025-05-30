package com.humanitas.backend.Controller;

import com.humanitas.backend.entity.DiaSemana;
import com.humanitas.backend.entity.EstadoReserva; // Importar si se usa como PathVariable o RequestParam
import com.humanitas.backend.entity.Reserva;
import com.humanitas.backend.entity.ReservasPorDia;
import com.humanitas.backend.repository.ReservaRepository;
import com.humanitas.backend.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // Para parsear LocalDate
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate; // Importar LocalDate
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaRepository reservaRepository;


    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Capturar excepciones del servicio (ej. Usuario no encontrado, Producto no disponible)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReservaPorId(@PathVariable int id) {
        Optional<Reserva> reserva = reservaService.obtenerReservaPorId(id);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaService.obtenerTodasLasReservas();
    }

    // --- Nuevos Endpoints de ejemplo ---

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerReservasPorUsuarioId(@PathVariable Long usuarioId) {
        try {
            List<Reserva> reservas = reservaService.obtenerReservasPorUsuarioId(usuarioId);
            if (reservas.isEmpty()) {
                throw new RuntimeException("No se encontraron reservas para el usuario con ID: " + usuarioId);
            }
            return ResponseEntity.ok(reservas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Reserva>> obtenerReservasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Reserva> reservas = reservaService.obtenerReservasPorFecha(fecha);
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> obtenerReservasPorEstado(@PathVariable EstadoReserva estado) {
        List<Reserva> reservas = reservaService.obtenerReservasPorEstado(estado);
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }

    // Actualizar una reserva (principalmente para cambiar el estado, ej. cancelar)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable int id, @RequestBody Reserva reservaActualizada) {
        // El body de reservaActualizada podría solo contener el nuevo 'estado'
        // o la nueva 'fechaReserva' si se permite modificarla.
        try {
            Reserva reserva = reservaService.actualizarReserva(id, reservaActualizada);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Si la reserva no se encuentra
        }
    }

    @GetMapping("/usuario/{usuarioId}/menu/{menuId}")
    public ResponseEntity<?> obtenerReservaPorUsuarioYMenu(@PathVariable Long usuarioId, @PathVariable Long menuId) {
        Optional<Reserva> reserva = reservaRepository.findByUsuarioIdAndMenuSemanalId(usuarioId, menuId);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Eliminar una reserva (generalmente no recomendado, se prefiere cancelar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable int id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (RuntimeException e) {
            // Si el servicio lanza una excepción cuando la reserva no se encuentra
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reportes/ventas-almuerzos-semanal")
    public ResponseEntity<List<Map<String, Object>>> obtenerVentasAlmuerzosPorSemana() {
        List<Map<String, Object>> reporte = reservaService.obtenerVentasAlmuerzosPorSemana();
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/reportes/reservas-por-dia/{dia}")
    public ResponseEntity<ReservasPorDia> obtenerReservasPorDia(@PathVariable DiaSemana dia) {
        ReservasPorDia resultado = reservaService.obtenerUsuariosPorDia(dia);
        return ResponseEntity.ok(resultado);
    }
}