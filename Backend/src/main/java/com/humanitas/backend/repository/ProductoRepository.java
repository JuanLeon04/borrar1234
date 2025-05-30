package com.humanitas.backend.repository;

import com.humanitas.backend.entity.CategoriaProducto;
import com.humanitas.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> { // Producto y el tipo de su ID (int)

    // Métodos de búsqueda personalizados (Spring Data JPA los implementará automáticamente)

    // Buscar productos por nombre (podría devolver varios si los nombres no son únicos)
    List<Producto> findByNombreContainingIgnoreCase(String nombre); // Busca nombres que contengan la cadena, ignorando mayúsculas/minúsculas

    // Buscar productos por categoría
    List<Producto> findByCategoria(CategoriaProducto categoria);

    // Buscar productos dentro de un rango de precios
    List<Producto> findByPrecioBetween(int precioMin, int precioMax);

    // Buscar un producto por su nombre exacto (si se espera que sea único)
    Optional<Producto> findByNombre(String nombre);

}