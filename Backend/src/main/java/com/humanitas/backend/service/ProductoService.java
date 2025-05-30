package com.humanitas.backend.service;

import com.humanitas.backend.entity.CategoriaProducto;
import com.humanitas.backend.entity.Producto;
import com.humanitas.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo producto
    @Transactional
    public Producto crearProducto(Producto producto) {
        // Aquí se podrían añadir validaciones adicionales antes de guardar,
        // por ejemplo, verificar si ya existe un producto con el mismo nombre.
        Optional<Producto> productoExistente = productoRepository.findByNombre(producto.getNombre());
        if (productoExistente.isPresent()) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + producto.getNombre());
        }
        return productoRepository.save(producto);
    }

    // Obtener un producto por su ID
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(int id) {
        return productoRepository.findById(id);
    }

    // Obtener todos los productos
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // Obtener productos por nombre (búsqueda flexible)
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Obtener productos por categoría
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCategoria(CategoriaProducto categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Obtener productos por rango de precio
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorRangoDePrecio(int precioMin, int precioMax) {
        if (precioMin > precioMax) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el precio máximo.");
        }
        return productoRepository.findByPrecioBetween(precioMin, precioMax);
    }

    // Actualizar un producto existente
    @Transactional
    public Producto actualizarProducto(int id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Verificar si el nuevo nombre ya está en uso por OTRO producto
        Optional<Producto> productoConNuevoNombre = productoRepository.findByNombre(productoActualizado.getNombre());
        if (productoConNuevoNombre.isPresent() && productoConNuevoNombre.get().getId() != id) {
            throw new RuntimeException("Ya existe otro producto con el nombre: " + productoActualizado.getNombre());
        }

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setCategoria(productoActualizado.getCategoria());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setImagen(productoActualizado.getImagen()); // Permitir actualizar la imagen

        return productoRepository.save(productoExistente);
    }

    // Eliminar un producto por su ID
    @Transactional
    public void eliminarProducto(int id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id + " para eliminar.");
        }
        // Aquí podrías añadir lógica para verificar si el producto está siendo referenciado
        // en alguna reserva o inventario antes de permitir la eliminación,
        // o manejarlo con restricciones de clave foránea en la base de datos.
        productoRepository.deleteById(id);
    }
}