import React, { useEffect, useState } from "react";
import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";
import { useAuth } from "../../context/AuthContext";
import { BASE_URL } from '../../config/apiConfig';

import {
  PieChart, Pie, Cell, Legend,
  BarChart, Bar, XAxis, YAxis,
  Tooltip, CartesianGrid, ResponsiveContainer
} from 'recharts';

import "./reportes.css";

const Reportes = () => {
  const { user, isAuthenticated } = useAuth();
  const [ventasAlmuerzos, setVentasAlmuerzos] = useState([]);

  const diasSemana = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES"];
  const [reservasPorDia, setReservasPorDia] = useState({});


  useEffect(() => {
    fetch(`http://${BASE_URL}/reservas/reportes/ventas-almuerzos-semanal`)
      .then((res) => res.json())
      .then((data) => setVentasAlmuerzos(data))
      .catch((err) => console.error("Error al cargar las ventas:", err));


    const fetchReservas = async () => {
    const resultados = {};
    for (let dia of diasSemana) {
      try {
        const res = await fetch(`http://${BASE_URL}/reservas/reportes/reservas-por-dia/${dia}`);
        const data = await res.json();
        resultados[dia] = data.usuarios;
      } catch (error) {
        console.error(`Error al obtener reservas para ${dia}:`, error);
      }
    }
    setReservasPorDia(resultados);
  };

  fetchReservas();
  }, []);

  return (
    <main className="page-wrapper">
      <Header />

      {/* Ventas Semanales */}
      <div className="container-fluid d-flex">
        <div className="container justify-content-center align-content-center text-center m-5">
          <p className="h5">Ventas de almuerzos semanales</p>
          <ResponsiveContainer width="100%" height={400}>
            <BarChart width={400} height={300} data={ventasAlmuerzos}>
              <CartesianGrid stroke="#ccc" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="ventas" fill="#99cc33" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
      
      <div className="container my-5">
        <h4 className="text-center mb-4">Reservas por día</h4>
        {diasSemana.map((dia) => (
            <div key={dia} className="mb-4">
            <h5 className="text-primary">{dia}</h5>
            {reservasPorDia[dia]?.length > 0 ? (
                <table className="table table-sm table-bordered">
                <thead className="table-light">
                    <tr>
                    <th>Nombre</th>
                    <th>Apellidos</th>
                    <th>Correo</th>
                    </tr>
                </thead>
                <tbody>
                    {reservasPorDia[dia].map((usuario) => (
                    <tr key={usuario.id}>
                        <td>{usuario.nombre}</td>
                        <td>{usuario.apellidos}</td>
                        <td>{usuario.correo}</td>
                    </tr>
                    ))}
                </tbody>
                </table>
            ) : (
                <p className="text-muted">Sin reservas</p>
            )}
            </div>
        ))}
        </div>


      <Footer />
    </main>
  );
};

export default Reportes;