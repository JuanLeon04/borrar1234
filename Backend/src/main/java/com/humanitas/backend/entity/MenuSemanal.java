package com.humanitas.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_semanal")
public class MenuSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // int32 (int en Java)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15) // LUNES, MARTES, etc.
    private DiaSemana dia;

    @Column(nullable = false, length = 150)
    private String nombrePlato;

    @Lob // Para textos más largos, si la descripción puede ser extensa
    @Column(columnDefinition = "TEXT")
    private String descripcionPlato;

    @Column(length = 255) // Para URL o ruta de imagen
    private String imagenPlato;

    @Column(nullable = false)
    private int disponibilidadPlato; // Cantidad disponible de este plato para ese día

    @Column(nullable = false)
    private double precio; // Nuevo campo para el precio

    @Column(name = "hora_limite_cancelacion")
    private String horaLimiteCancelacion;


    // Por ahora, omitiré un campo directo de tipo 'Reserva'

    // Constructores
    public MenuSemanal() {
    }

    public MenuSemanal(DiaSemana dia, String nombrePlato, String descripcionPlato, String imagenPlato, int disponibilidadPlato, double precio, String horaLimiteCancelacion) {
        this.dia = dia;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.imagenPlato = imagenPlato;
        this.disponibilidadPlato = disponibilidadPlato;
        this.precio = precio;
        this.horaLimiteCancelacion = horaLimiteCancelacion;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public String getImagenPlato() {
        return imagenPlato;
    }

    public void setImagenPlato(String imagenPlato) {
        this.imagenPlato = imagenPlato;
    }

    public int getDisponibilidadPlato() {
        return disponibilidadPlato;
    }

    public void setDisponibilidadPlato(int disponibilidadPlato) {
        this.disponibilidadPlato = disponibilidadPlato;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getHoraLimiteCancelacion() {
    return horaLimiteCancelacion;
    }
    public void setHoraLimiteCancelacion(String horaLimiteCancelacion) {
        this.horaLimiteCancelacion = horaLimiteCancelacion;
    }

    @Override
    public String toString() {
        return "MenuSemanal{" +
                "id=" + id +
                ", dia=" + dia +
                ", nombrePlato='" + nombrePlato + '\'' +
                ", descripcionPlato='" + descripcionPlato + '\'' +
                ", imagenPlato='" + imagenPlato + '\'' +
                ", disponibilidadPlato=" + disponibilidadPlato +
                ", precio=" + precio + 
                ", horaLimiteCancelacion='" + horaLimiteCancelacion + '\'' +
                '}';
    }
}