import React from "react";
import { useState } from "react";
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from "react-router-dom";

import { BASE_URL } from '../../config/apiConfig';

const Login = () => {

  // Login
  const {login } = useAuth();
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({ correo: "", contrasena: "" });

  const handleChangeLogin = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  }

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log("Credenciales ingresadas:", credentials);
    const success = await login(credentials.correo, credentials.contrasena);
    if (success) {
      navigate("/home");
    } else {
      alert("Usuario o contraseña incorrectos.");
    }
  };

  //Registro
  const [registro, setRegistro] = useState({
    apellidos: "",
    codigoEstudiante: "",
    contrasena: "",
    correo: "",
    nombre: "",
    rol: "ESTUDIANTE"
  });
  const handleChangeRegister = (e) => {
    const { id, value } = e.target;
    console.log(`Campo cambiado: ${id}, Nuevo valor: ${value}`); // Log para verificar cambios
    setRegistro((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };
  const crearUsuario = async (usuario) => {
    try {
      console.log("Enviando datos al servidor:", usuario); // Log para verificar datos enviados
      const response = await fetch(`http://${BASE_URL}/usuarios`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(usuario),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Usuario creado:", data);
        alert("Usuario creado con éxito");

        // Intentar iniciar sesión directamente con los datos del usuario
        const success = await login(usuario.correo, usuario.contrasena);
        if (success) {
          navigate("/home");
        } else {
          alert("El usuario fue creado, pero no se pudo iniciar sesión automáticamente.");
        }

      } else {
        console.error("Error al crear el usuario");
        alert("Error al crear el usuario");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("No se pudo conectar con el servidor");
    }
  };
  const handleRegistroSubmit = (e) => {
    e.preventDefault();
    const nuevoUsuario = {
      apellidos: registro.apellidos,
      codigoEstudiante: registro.codigoEstudiante,
      contrasena: registro.contrasena,
      correo: registro.correo,
      nombre: registro.nombre,
      rol: "ESTUDIANTE"
    };
    console.log("Datos del usuario a registrar:", nuevoUsuario); // Log para verificar datos
    crearUsuario(nuevoUsuario);

  }




  return (
    <>
      <div className="container-fluid p-0 border-0"  style={{overflowX: "hidden"  }}>
        <div className="row justify-content-center">
          <div className="col-12 col-md-6 d-flex justify-content-center align-items-center" style={{ height:"100vh", backgroundColor: "#ffffff" }}>

            <form onSubmit={handleLogin}>
              <div className="mb-3">
                <h1 className="p-4">Iniciar sesion</h1>
                <label htmlFor="email" className="form-label">Correo electrónico</label>
                <input
                  type="email"
                  className="form-control"
                  id="correo"
                  placeholder="ejemplo@correo.com"
                  name="correo"
                  onChange={handleChangeLogin}
                  value={credentials.correo}
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="password" className="form-label">Contraseña</label>
                <input
                  type="password"
                  className="form-control"
                  id="contrasena"
                  name="contrasena"
                  placeholder="••••••••"
                  onChange={handleChangeLogin}
                  value={credentials.contrasena}
                  required
                />
              </div>

              <div className="d-flex justify-content-between align-items-center mb-3">
                <div className="form-check">
                  <input className="form-check-input" type="checkbox" id="remember" />
                  <label className="form-check-label" htmlFor="remember">
                    Recuérdame
                  </label>
                </div>
                <a href="#" className="text-decoration-none">¿Olvidaste tu contraseña?</a>
              </div>

              <button type="submit" className="btn btn-primary w-100">Entrar</button>
            </form>


          </div>
          <div className="col-12 col-md-6 d-flex justify-content-center align-items-center" style={{ height:"100vh", backgroundColor: "#99cc33" }}>

            <form onSubmit={handleRegistroSubmit}>
              <div className="mb-3">
                <h1 className="p-4">Registrarse</h1>
                <label className="form-label">Nombre</label>
                <input
                  type="text"
                  className="form-control"
                  id="nombre"
                  placeholder="Primer y/ó Segundo nombre"
                  onChange={handleChangeRegister}
                  value={registro.nombre}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Apellido</label>
                <input
                  type="text"
                  className="form-control"
                  id="apellidos"
                  placeholder="Apellidos"
                  onChange={handleChangeRegister}
                  value={registro.apellidos}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Codigo Estudiante</label>
                <input
                  type="number"
                  className="form-control"
                  id="codigoEstudiante"
                  placeholder="2222222"
                  onChange={handleChangeRegister}
                  value={registro.codigoEstudiante}
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="email" className="form-label">Correo</label>
                <input
                  type="email"
                  className="form-control"
                  id="correo"
                  onChange={handleChangeRegister}
                  value={registro.correo}
                  placeholder="usuario@correo.com"
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="password" className="form-label">Contraseña</label>
                <input
                  type="password"
                  className="form-control"
                  id="contrasena"
                  onChange={handleChangeRegister}
                  value={registro.contrasena}
                  placeholder="********"
                  required
                />
              </div>

              <div className="d-flex justify-content-between align-items-center mb-3">
                <div className="form-check">
                  <input className="form-check-input" type="checkbox" id="recibir_email" />
                  <label className="form-check-label" htmlFor="recibir_email">
                    Recibir notificaciones vía email
                  </label>
                </div>
              </div>

              <button type="submit" onClick={() => console.log("Formulario de inicio de sesión enviado")} Registrarse className="btn btn-secundary w-100">Registrarse</button>
            </form>

          </div>
        </div>
      </div>
    </>
  );
};

export default Login;
