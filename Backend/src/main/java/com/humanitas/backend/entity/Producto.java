package com.humanitas.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productos") // Especificamos el nombre de la tabla
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // int32 (int en Java)

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING) // Guardará "BEBIDA" o "COMIDA" en la BD
    @Column(nullable = false)
    private CategoriaProducto categoria;

    @Column(nullable = false)
    private int precio; // precio de tipo int

    @Column(length = 255) // Longitud para una URL o ruta de archivo de imagen
    private String imagen; // String para la imagen (URL o ruta)

    // Constructores
    public Producto() {
    }

    public Producto(String nombre, CategoriaProducto categoria, int precio, String imagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // Considera agregar toString(), equals(), y hashCode() o usar Lombok

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + categoria +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}