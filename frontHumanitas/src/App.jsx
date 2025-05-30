import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import "./styles/App.css";

import Home from './pages/Home/home.jsx';
import Login from './pages/Login/login.jsx';
import Reservas from './pages/Reservas/reservas.jsx';
import Administracion from './pages/Administracion/administracion.jsx';
import Reportes from "./pages/Reportes/reportes.jsx";
import Historial from "./pages/Historial/historial.jsx";
import Contacto from "./pages/Contacto/contacto.jsx";
import Productos from "./pages/Productos/productos.jsx";
import AdministrarProductos from "./pages/Administracion/Productos/administrarProductos.jsx";

import { AuthProvider } from './context/AuthContext.jsx';


function App() {
    return (
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/home" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/reservas" element={<Reservas />} />
            <Route path="/historial" element={<Historial />} />
            <Route path="/contacto" element={<Contacto />} />
            <Route path="/productos" element={<Productos />} />

            <Route path="/admin/menu" element={<Administracion />} />
            <Route path="/admin/productos" element={<AdministrarProductos />} />
            <Route path="/admin/metricas" element={<Reportes />} />
          </Routes>
        </Router>
      </AuthProvider>
    );
  }

  export default App;