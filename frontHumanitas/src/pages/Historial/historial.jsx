import React, { useEffect, useState } from "react";

import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";

import { BASE_URL } from '../../config/apiConfig';

import { useAuth } from "../../context/AuthContext";

const Historial = () => {

  const [historial, setHistorial] = useState([]);
  const [resumen, setResumen] = useState({ entradas: 0, total: 0 });
  const { user } = useAuth();

  useEffect(() => {
  const fetchHistorial = async () => {
    try {
      const userId = user.id;
      console.log("ID de usuario:", userId);
      const res = await fetch(`http://${BASE_URL}/reservas/usuario/${userId}`);
      if (res.ok) {
        const data = await res.json();
        setHistorial(data);
        console.log("Historial de reservas:", data);
        // Calcular resumen
        let total = 0;
        let entradas = 0;
        data.forEach(reserva => {
          total += reserva.menuSemanal.precio;
          entradas += 1;
        });
        setResumen({ entradas, total });
      } else {
        const errorMsg = await res.text(); // Lee el mensaje de error del backend
        console.log("Historial reserva error: " + errorMsg);
      }

    } catch (err) {
      console.error("Error al cargar el historial:", err);
    }
  };
  fetchHistorial();
  }, [user.id]);

  return (
    <>
      <Header />
      <div className="container">
        <h1 className="text-center mt-4 mb-4">Historial</h1>
        <p className="text-center">Total entradas: {resumen.entradas}</p>
        <p className="text-center">Total gastado: {resumen.total}</p>

        <table class="table table-striped">
            <thead >
                <tr>
                    <td>#</td>
                    <td>Fecha</td>
                    <td>Tipo</td>
                    <td>Dia Reservado</td>
                    <td>Estado</td>
                    <td>Precio</td>
                </tr>
            </thead>
              <tbody>
                {historial.map((item, index) => (
                  <tr key={index}>
                    <td>{index}</td>
                    <td>{item.fechaReserva}</td>
                    <td>Almuerzo</td>
                    <td>{item.menuSemanal.dia}</td>
                    <td>{item.estado}</td>
                    <td>{item.menuSemanal.precio}</td>
                  </tr>
                ))}
              </tbody>
        </table>
      </div>
      <Footer />
    </>
  );
};

export default Historial;