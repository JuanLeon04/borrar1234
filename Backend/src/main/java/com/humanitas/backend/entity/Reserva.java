package com.humanitas.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate; // Cambiado de LocalDateTime a LocalDate
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Se mantiene como int (equivalente a int32)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva; // Cambiado a LocalDate para formato año/mes/día

    @Enumerated(EnumType.STRING) // Guarda el Enum como String ("CONFIRMADO", "CANCELADO")
    @Column(nullable = false)
    private EstadoReserva estado; // Nuevo campo para el estado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_semanal_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MenuSemanal menuSemanal;


    // Constructores
    public Reserva() {
    }

    public Reserva(Usuario usuario, MenuSemanal menuSemanal, LocalDate fechaReserva, EstadoReserva estado) {
        this.usuario = usuario;
        this.menuSemanal = menuSemanal;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }



    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

     public MenuSemanal getMenuSemanal() {
        return menuSemanal;
    }

    public void setMenuSemanal(MenuSemanal menuSemanal) {
        this.menuSemanal = menuSemanal;
    }
}