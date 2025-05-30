package com.humanitas.backend.Controller;

import com.humanitas.backend.entity.CategoriaProducto;
import com.humanitas.backend.entity.Producto;
import com.humanitas.backend.entity.Rol;
import com.humanitas.backend.entity.Usuario;
import com.humanitas.backend.service.ProductoService;
import com.humanitas.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite el acceso desde cualquier origen (ajustar en producción)
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Devuelve el mensaje de error del servicio
        }
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    // Endpoint para obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable int id) {
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para buscar productos por nombre (parámetro de consulta)
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Producto>> buscarProductosPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build(); // O notFound() si se prefiere
        }
        return ResponseEntity.ok(productos);
    }

    // Endpoint para obtener productos por categoría (parámetro de ruta)
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@PathVariable CategoriaProducto categoria) {
        List<Producto> productos = productoService.obtenerProductosPorCategoria(categoria);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    // Endpoint para obtener productos por rango de precio (parámetros de consulta)
    @GetMapping("/buscar/precio")
    public ResponseEntity<?> obtenerProductosPorRangoDePrecio(
            @RequestParam int precioMin,
            @RequestParam int precioMax) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorRangoDePrecio(precioMin, precioMax);
            if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(productos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable int id, @RequestBody Producto productoActualizado, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            Producto producto = productoService.actualizarProducto(id, productoActualizado);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            // Si el producto no se encuentra o el nombre ya existe para otro producto
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (RuntimeException e) {
            // Si el producto no se encuentra
            return ResponseEntity.notFound().build();
        }
    }
}