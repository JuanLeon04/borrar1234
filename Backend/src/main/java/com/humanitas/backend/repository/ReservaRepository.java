package com.humanitas.backend.repository;

import com.humanitas.backend.entity.DiaSemana;
import com.humanitas.backend.entity.EstadoReserva;
import com.humanitas.backend.entity.Reserva;
//import com.humanitas.backend.entity.ReservaMenuSemanal;

// import com.humanitas.backend.entity.Usuario; // No es necesario si usas IDs
// import com.humanitas.backend.entity.Inventario; // No es necesario si usas IDs
import com.humanitas.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar reservas por ID de usuario
    List<Reserva> findByUsuarioId(Long usuarioId); // Asumiendo que Usuario.id es Long

    // Buscar reservas por ID del producto reservado
   // List<Reserva> findByProductoReservadoId(Long productoId); // Asumiendo que Inventario.id es Long

    // Buscar reservas por fecha específica
    List<Reserva> findByFechaReserva(LocalDate fechaReserva);

    // Buscar reservas de un usuario para una fecha específica
    List<Reserva> findByUsuarioIdAndFechaReserva(Long usuarioId, LocalDate fechaReserva);

    // Buscar reservas por estado
    List<Reserva> findByEstado(EstadoReserva estado);

    // Buscar reservas de un usuario por estado
    List<Reserva> findByUsuarioIdAndEstado(Long usuarioId, EstadoReserva estado);

   // List<ReservaMenuSemanal> findByMenuSemanalId(Long menuSemanalId);

    // Buscar reservas por producto y fecha
    //List<Reserva> findByProductoReservadoIdAndFechaReserva(Long productoId, LocalDate fechaReserva);

    //List<Reserva> findByUsuarioIdAndProductoReservadoIdAndFechaReserva(Long usuarioId, Long productoReservadoId, LocalDate fechaReserva);

    List<Reserva> findByMenuSemanalId(Integer menuSemanalId);

    Optional<Reserva> findByUsuarioIdAndMenuSemanalId(Long usuarioId, Long menuSemanalId);

    @Query("SELECT FUNCTION('DAYNAME', r.fechaReserva), COUNT(r) FROM Reserva r GROUP BY FUNCTION('DAYNAME', r.fechaReserva)")
    List<Object[]> contarReservasPorDia();

    @Query("SELECT m.dia, COUNT(r) " +
            "FROM Reserva r " +
            "JOIN r.menuSemanal m " +
            "WHERE r.estado = 'CONFIRMADO' " +
            "GROUP BY m.dia")
    List<Object[]> contarReservasConfirmadasPorDiaDelMenu();

    @Query("SELECT r FROM Reserva r " +
            "WHERE r.usuario.id = :usuarioId " +
            "AND r.menuSemanal.dia = :dia")
    List<Reserva> findByUsuarioIdAndDiaSemana(@Param("usuarioId") Long usuarioId,
                                              @Param("dia") DiaSemana dia);

    @Query("SELECT r.usuario FROM Reserva r WHERE r.menuSemanal.dia = :dia AND r.estado = 'CONFIRMADO'")
    List<Usuario> findUsuariosPorDiaReserva(@Param("dia") DiaSemana dia);

    @Query("SELECT COUNT(r), COALESCE(SUM(m.precio),0) FROM Reserva r JOIN r.menuSemanal m WHERE r.usuario.id = :usuarioId AND r.estado = 'CONFIRMADO'")
    Object[] obtenerResumenReservasPorUsuario(@Param("usuarioId") Long usuarioId);

}