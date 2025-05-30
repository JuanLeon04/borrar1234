import React from "react";
import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";
import { BASE_URL } from '../../config/apiConfig';

const Contacto = () => {
  return (
    <>
      <Header />
      <div className="container py-4">
        <h1 className="mb-4">Contáctanos</h1>
        <p>
          ¿Tienes preguntas, sugerencias o quieres hacer un pedido especial?
          Escríbenos a través del siguiente formulario o llámanos.
        </p>
        <form className="mb-5">
          <div className="mb-3">
            <label htmlFor="nombre" className="form-label">Nombre</label>
            <input type="text" className="form-control" id="nombre" placeholder="Tu nombre" />
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">Correo electrónico</label>
            <input type="email" className="form-control" id="email" placeholder="tu@email.com" />
          </div>
          <div className="mb-3">
            <label htmlFor="mensaje" className="form-label">Mensaje</label>
            <textarea className="form-control" id="mensaje" rows="4" placeholder="Escribe tu mensaje aquí..."></textarea>
          </div>
          <button type="submit" className="btn btn-primary">Enviar mensaje</button>
        </form>
      </div>
      <Footer />
    </>
  );
};

export default Contacto;