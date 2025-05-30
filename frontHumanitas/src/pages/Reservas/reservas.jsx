import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

import { BASE_URL } from '../../config/apiConfig';

import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";
import { useAuth } from "../../context/AuthContext";

const Reservas = () => {

  // Si el usuario esta logeado
  const { user, isAuthenticated } = useAuth();

  // Cargar reservas desde API json
  const [menu, setMenu] = useState([]);
  useEffect(() => {
    fetch(`http://${BASE_URL}/menu-semanal`)
      .then((res) => res.json())
      .then((data) => setMenu(data)) 
      .catch((err) => console.error("Error al cargar el menú:", err));
  }, []);

  console.log("Menú semanal:", menu);
  const [diaSeleccionado, setDiaSeleccionado] = useState("");
  const [accion, setAccion] = useState("");
  const [showModal, setShowModal] = useState(false); 

  // Abre el modal y actualiza el día
  const abrirModal = (dia, accion) => {
    console.log("Abriendo modal para el día:", dia, "Acción:", accion);
    setDiaSeleccionado(dia);
    setAccion(accion)
    setShowModal(true);
  };

  // Confirmación de la reserva
  const confirmarCancelarReserva = () => {
    setShowModal(false);
    if (accion == "Reservar") {
      confirmarReserva();
    } else if (accion == "Cancelar") {
      cancelarReserva();
    }
  };


  // Buscar el id del menú semanal por día
  const getMenuByDia = (dia) => {
    const item = menu.find((m) => m.dia === dia);
    console.log(item);
    return item && item ? item : null;
  };


  // Cancelar reserva (puedes adaptar el endpoint según tu backend)
  const cancelarReserva = async () => {
    if (user.rol != "ESTUDIANTE") {
      alert("Debes iniciar sesión como estudiante para cancelar una reserva.");
      return;
    }

    const menu = getMenuByDia(diaSeleccionado);

    try {
      // Paso 1: Buscar la reserva por usuario y menú (idealmente desde backend, aquí simulado)
      const buscarUrl = `http://${BASE_URL}/reservas/usuario/${user.id}/menu/${menu.id}`;
      const buscarResp = await fetch(buscarUrl);

      if (!buscarResp.ok) {
        alert("No se encontró una reserva existente para cancelar.");
        return;
      }

      const reservaExistente = await buscarResp.json();

      // Paso 2: Modificar el estado y enviar PUT a /api/reservas/{id}
      const reservaCancelada = {
        ...reservaExistente,
        estado: "CANCELADO"
      };

      const response = await fetch(`http://${BASE_URL}/reservas/${reservaExistente.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reservaCancelada),
      });

      if (response.ok) {
        alert(`¡Reserva cancelada con éxito para el día ${diaSeleccionado}!`);
      } else {
        const errorMsg = await response.text();
        alert("No se pudo cancelar la reserva: " + errorMsg);
      }

    } catch (error) {
      console.error("Error al cancelar la reserva:", error);
      alert("Error al conectar con el servidor.");
    }
  };


  // Hacer reserva (POST al backend)
  const confirmarReserva = async () => {
    if (user.rol != "ESTUDIANTE") {
      alert("Debes iniciar sesión como estudiante para reservar.");
      return;
    }
    const menu = getMenuByDia(diaSeleccionado);
    console.log("ID del menú para el día seleccionado:", menu.id);
    console.log("Usuario ID:", user.id);
    if (menu == null) {
      alert("No se encontró el menú para ese día.");
      return;
    }
    // Construir objeto reserva según backend
    const reserva = {
      estado: "CONFIRMADO",
      fechaReserva: new Date().toISOString().slice(0, 10), ////// YYYY-MM-DD
      menuSemanal: menu,
      usuario: user,
      
    };
    console.log("Reserva a enviar:", reserva);
    try {
      const response = await fetch(`http://${BASE_URL}/reservas`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reserva),
      });
      if (response.ok) {
        alert(`¡Reserva confirmada para el día ${diaSeleccionado}!`);
      } else {
        const errorMsg = await response.text(); // Lee el mensaje de error del backend
        alert("No se pudo realizar la reserva: " + errorMsg);
      }
    } catch (error) {
      alert("Error al conectar con el servidor.",error);
    }
  };

  return (
    <>
      <Header />
      <div className="container">
        <h1 className="text-center mt-4 mb-4">Menú semanal</h1>
        <div className="row justify-content-center gx-0 mb-4">
          {menu.map((item, index) => (
            <div className="col-6 col-md-4 col-xl-3" key={index}>
              <div className="card m-2">
                <img className="img-fluid rounded" alt={`Imagen del día ${item.dia}`} src={item.imagenPlato}/>
                <div className="card-body">
                  <h1 className="card-title">{item.dia}</h1>
                  <h4 className="card-title">{item.nombrePlato}</h4>
                  <p className="card-text">{item.descripcionPlato}</p>
                  <p className="card-text">${item.precio}</p>
                  <button className="btn btn-primary w-100 mb-2" onClick={() => abrirModal(item.dia, "Reservar")} disabled={user.rol != "ESTUDIANTE"}>Reservar</button>
                  <button className="btn btn-danger w-100" onClick={() => abrirModal(item.dia, "Cancelar")} disabled={user.rol != "ESTUDIANTE"}>Cancelar reserva</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <>
          <div className="modal d-block show" tabIndex="-1" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Confirmación</h5>
                  <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                </div>
                <div className="modal-body text-center">
                  <p> Estás a punto de <strong>{accion}</strong> una reserva para el día{" "}<strong>{diaSeleccionado}</strong></p>
                  <p>¿Deseas continuar?</p>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-primary" onClick={() => {confirmarCancelarReserva()}}>¡{accion}!</button>
                  <button type="button" className="btn btn-danger" onClick={() => setShowModal(false)}>Cancelar</button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
      <Footer />
    </>
  );
};

export default Reservas;