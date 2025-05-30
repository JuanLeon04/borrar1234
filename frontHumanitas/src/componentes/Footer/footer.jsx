import React from 'react';
import "./footer.css";
import { BASE_URL } from '../../config/apiConfig';

function Footer() {
  return (
    <footer className="footer py-3 container-fluid w-100">
      <div className="text-center">
        <p className="mb-1">&copy; {new Date().getFullYear()} Café Humanitas. Todos los derechos reservados.</p>
        <p className="mb-0">
          Desarrollado con ❤️ usando React y Bootstrap.
        </p>
      </div>
    </footer>
  );
}

export default Footer;