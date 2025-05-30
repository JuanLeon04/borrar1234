import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import { BASE_URL } from '../../config/apiConfig';

import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";

const Productos = () => {
  const [productos, setProductos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const location = useLocation();

  // Obtener query de la URL
  const query = new URLSearchParams(location.search);
  const categoriaFiltro = query.get("categoria");

  useEffect(() => {
    fetch(`http://${BASE_URL}/productos`) // endpoint -------------------------------------------------------------------
      .then((res) => res.json())
      .then((data) => {
        let lista = data;

        // Si hay filtro por categoría, aplicarlo
        if (categoriaFiltro) {
          lista = lista.filter(
            (producto) => producto.categoria === categoriaFiltro
          );
        }

        setProductos(lista);
        setIsLoading(false);
      })
      .catch((err) => {
        console.error("Error al cargar productos:", err);
        setIsLoading(false);
      });
  }, [categoriaFiltro]); // ← importante: se vuelve a correr si cambia

  if (isLoading) {
    return (
      <>
        <Header />
        <div className="container text-center mt-4">
          <h2>Cargando productos...</h2>
        </div>
        <Footer />
      </>
    );
  }

  return (
    <>
      <Header />
      <div className="container">
        <h1 className="text-center mt-4 mb-4">
          {categoriaFiltro ? `Categoría: ${categoriaFiltro}` : "Todos los productos"}
        </h1>
        <div className="row">
          {productos.map((item) => (
            <div className="col-6 col-md-4 col-lg-3 mb-4" key={item.id}>
              <div className="card">
              <img className="img-fluid rounded" alt={`Imagen de referencia ${item.nombre}`} src={item.imagen}/>
                <div className="card-body">
                  <h4 className="card-title"><b>{item.nombre}</b></h4>
                  <p className="card-text">
                    Categoría: {item.categoria} <br />
                    Precio: ${item.precio}
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Productos;