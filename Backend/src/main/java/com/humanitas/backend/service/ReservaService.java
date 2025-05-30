package com.humanitas.backend.service;

import com.humanitas.backend.entity.*;
import com.humanitas.backend.repository.ReservaRepository;
import com.humanitas.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanitas.backend.repository.MenuSemanalRepository;


import java.time.LocalDate;
import java.util.*;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MenuSemanalRepository menuSemanalRepository;

    @Transactional
    public Reserva crearReserva(Reserva reserva) {
        Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + reserva.getUsuario().getId()));
        reserva.setUsuario(usuario);

        MenuSemanal menuSemanal = menuSemanalRepository.findById(reserva.getMenuSemanal().getId())
                .orElseThrow(() -> new RuntimeException("MenuSemanal no encontrado con ID: " + reserva.getMenuSemanal().getId()));
        reserva.setMenuSemanal(menuSemanal);

        // Validación de fecha
        if (reserva.getFechaReserva().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de reserva no puede ser en el pasado.");
        }

        // 🛡️ Nueva validación: evitar reservas duplicadas para el mismo menú en la misma semana
        List<Reserva> reservasExistentes = reservaRepository.findByUsuarioIdAndDiaSemana(
                usuario.getId(),
                menuSemanal.getDia()
        );

        if (!reservasExistentes.isEmpty()) {
            throw new RuntimeException("Ya existe una reserva para este usuario en el día: " + menuSemanal.getDia());
        }

        if (reserva.getEstado() == null) {
            reserva.setEstado(EstadoReserva.CONFIRMADO);
        }

        return reservaRepository.save(reserva);
    }


    @Transactional(readOnly = true)
    public Optional<Reserva> obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerReservasPorUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerReservasPorFecha(LocalDate fecha) {
        return reservaRepository.findByFechaReserva(fecha);
    }

    @Transactional(readOnly = true)
    public List<Reserva> obtenerReservasPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    @Transactional
    public Reserva actualizarReserva(Integer id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        if (reservaActualizada.getEstado() != null) {
            reservaExistente.setEstado(reservaActualizada.getEstado());
        }

        if (reservaActualizada.getFechaReserva() != null &&
                !reservaActualizada.getFechaReserva().isBefore(LocalDate.now()) &&
                reservaExistente.getEstado() == EstadoReserva.CONFIRMADO) {
            reservaExistente.setFechaReserva(reservaActualizada.getFechaReserva());
        }

        return reservaRepository.save(reservaExistente);
    }

    @Transactional
    public void eliminarReserva(Integer id) {
        reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada para eliminar con ID: " + id));
        reservaRepository.deleteById(id);
    }

    public List<Map<String, Object>> obtenerVentasAlmuerzosPorSemana() {
        List<Object[]> resultados = reservaRepository.contarReservasConfirmadasPorDiaDelMenu();
        List<Map<String, Object>> reporte = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("name", fila[0]);    // Día (ej. "LUNES")
            datos.put("ventas", fila[1]);  // Número de reservas confirmadas
            reporte.add(datos);
        }

        return reporte;
    }

    public ReservasPorDia obtenerUsuariosPorDia(DiaSemana dia) {
        List<Usuario> usuarios = reservaRepository.findUsuariosPorDiaReserva(dia);
        return new ReservasPorDia(dia, usuarios);
    }
}