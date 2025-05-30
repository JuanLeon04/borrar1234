package com.humanitas.backend.service;

import com.humanitas.backend.entity.Usuario;
import com.humanitas.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Es buena práctica para métodos que modifican datos

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional // Asegura que la operación sea atómica
    public Usuario crearUsuario(Usuario usuario) {
        // Aquí podrías añadir lógica de negocio antes de guardar,
        // por ejemplo, verificar si el codigoEstudiante o correo ya existen,
        // o hashear la contraseña si no se hace en otro lado (ej. un DTO o en el controller).
        // Por ahora, asumimos que la contraseña ya viene hasheada o se maneja en la entidad/DTO.
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true) // Optimización para operaciones de solo lectura
    public Optional<Usuario> obtenerUsuarioPorId(Long id) { // Cambiado int a Long
        return usuarioRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) { // Cambiado int a Long
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findById(id);
        if (usuarioExistenteOptional.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOptional.get();

            // Actualizar todos los campos relevantes
            // Se asume que el cliente envía todos los campos que quiere actualizar.
            // Si solo se envían algunos, necesitarías una lógica para actualizar solo los no nulos.
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellidos(usuarioActualizado.getApellidos());
            usuarioExistente.setCodigoEstudiante(usuarioActualizado.getCodigoEstudiante());
            usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
            usuarioExistente.setRol(usuarioActualizado.getRol());


            if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {

                // si no se hizo antes de llegar al servicio.
                usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
            }

            return usuarioRepository.save(usuarioExistente);
        } else {
            // que luego sea manejada por un @ControllerAdvice para devolver un 404.
            // Por ahora, devolvemos null para que el controlador devuelva 404.
            return null;
        }
    }

    @Transactional
    public boolean eliminarUsuario(Long id) { // Cambiado int a Long y el tipo de retorno
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true; // Eliminación exitosa
        } else {
            return false; // Usuario no encontrado
        }
    }
}