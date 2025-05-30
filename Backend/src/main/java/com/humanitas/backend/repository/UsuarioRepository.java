package com.humanitas.backend.repository;

import com.humanitas.backend.entity.Rol; // Importar el Enum Rol
import com.humanitas.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // Cambiado Integer a Long

    // Método para buscar un usuario por su ID
    Optional<Usuario> findById(Long id); // Cambiado Integer a Long
    // Método para buscar un usuario por su correo
    Optional<Usuario> findByCorreo(String correo);

    // Método para buscar un usuario por su código de estudiante
    Optional<Usuario> findByCodigoEstudiante(String codigoEstudiante);

    // Método para buscar un usuario por su nombre
    List<Usuario> findByNombre(String nombre); // Cambiado a List<Usuario> por si hay nombres repetidos

    // Método para buscar todos los usuarios con un rol específico
    List<Usuario> findByRol(Rol rol); // Cambiado el tipo de parámetro a Rol (el Enum)

}