package com.humanitas.backend.Controller;

import com.humanitas.backend.entity.DiaSemana;
import com.humanitas.backend.entity.MenuSemanal;
import com.humanitas.backend.entity.Rol;
import com.humanitas.backend.entity.Usuario;
import com.humanitas.backend.service.MenuSemanalService;
import com.humanitas.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Para Spring Security (ejemplo, no se implementa aquí completamente):
// import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/menu-semanal")
@CrossOrigin(origins = "*") // Ajustar en producción
public class MenuSemanalController {

    @Autowired
    private MenuSemanalService menuSemanalService;
    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para crear un nuevo ítem en el menú
    // TODO: Asegurar con Spring Security - @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<?> crearItemMenu(@RequestBody MenuSemanal menuItem, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
        
        try {
            MenuSemanal nuevoItem = menuSemanalService.crearItemMenu(menuItem);
            return new ResponseEntity<>(nuevoItem, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para obtener todos los ítems del menú
    @GetMapping
    public ResponseEntity<List<MenuSemanal>> obtenerTodoElMenu() {
        List<MenuSemanal> menu = menuSemanalService.obtenerTodoElMenu();
        return ResponseEntity.ok(menu);
    }

    // Endpoint para obtener un ítem del menú por su ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuSemanal> obtenerItemMenuPorId(@PathVariable int id) {
        Optional<MenuSemanal> menuItem = menuSemanalService.obtenerItemMenuPorId(id);
        return menuItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para obtener el menú de un día específico
    @GetMapping("/dia/{dia}")
    public ResponseEntity<List<MenuSemanal>> obtenerMenuPorDia(@PathVariable DiaSemana dia) {
        List<MenuSemanal> menuDelDia = menuSemanalService.obtenerMenuPorDia(dia);
        if (menuDelDia.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(menuDelDia);
    }

    // Endpoint para buscar ítems del menú por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<MenuSemanal>> buscarItemsMenuPorNombre(@RequestParam String nombre) {
        List<MenuSemanal> items = menuSemanalService.buscarItemsMenuPorNombre(nombre);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    // Endpoint para obtener ítems del menú que están disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<MenuSemanal>> obtenerItemsMenuDisponibles() {
        List<MenuSemanal> itemsDisponibles = menuSemanalService.obtenerItemsMenuDisponibles();
        if (itemsDisponibles.isEmpty()) {
            return ResponseEntity.noContent().build(); // O notFound()
        }
        return ResponseEntity.ok(itemsDisponibles);
    }


    // Endpoint para actualizar un ítem existente del menú
    // TODO: Asegurar con Spring Security - @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarItemMenu(@PathVariable int id, @RequestBody MenuSemanal menuActualizado, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            MenuSemanal itemActualizado = menuSemanalService.actualizarItemMenu(id, menuActualizado);
            return ResponseEntity.ok(itemActualizado);
        } catch (RuntimeException e) {
            // Podría ser por item no encontrado o por conflicto de nombre/día
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para eliminar un ítem del menú
    // TODO: Asegurar con Spring Security - @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItemMenu(@PathVariable int id, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            menuSemanalService.eliminarItemMenu(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (RuntimeException e) {
            // Si el ítem no se encuentra
            return ResponseEntity.notFound().build();
        }
    }

    // TODO: Asegurar con Spring Security - @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reiniciar")
    public ResponseEntity<?> reiniciarMenuLaboral(@RequestBody Map<String, String> body, @RequestParam("idUsuario") Long idUsuario) {
        // Verifica el rol del usuario
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getRol() != Rol.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            String horaLimiteCancelacion = body.get("horaLimiteCancelacion");
            menuSemanalService.reiniciarMenuLaboral(horaLimiteCancelacion);
            return ResponseEntity.ok("Menú semanal laboral reiniciado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al reiniciar el menú.");
        }
    }

}