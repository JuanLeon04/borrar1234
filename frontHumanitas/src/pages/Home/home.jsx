import React from "react";
import Header from "../../componentes/Header/header.jsx";
import Footer from "../../componentes/Footer/footer.jsx";
import "./home.css";
import imagen from "../../assets/imagen-home.jpeg"
import { Link } from "react-router-dom";
import { BASE_URL } from '../../config/apiConfig';


const Home = () => {
  

  return (
    <>
    <main className="page-wrapper">
      <Header/>
      <div className="top-div row m-0 p-0">
        <h1 className="px-lg-4 col-12 text-center text-md-start m-0 p-0">
          <span className="green">Café</span>   
          <span className="white">Humanitas</span>
        </h1>
      </div>

      <div className="container-fluid content-wrapper d-flex flex-column flex-md-row p-0 m-0 flex-grow-1">
        <div className="info d-flex justify-content-center align-items-center col-12 col-md-6 " >
          <p className="text-center">
            Descubre el sabor que despierta tus sentidos...
          </p>
      </div>

        <div className=" d-flex flex-row col-12 col-md-6 p-20 flex-grow-1">
          <Link to="/reservas" className="option d-flex justify-content-center align-items-center flex-grow-1 text-decoration-none">      
            <p>Ver<br/> Menú<br/>  Semanal<br/> </p>
          </Link>
          <Link to="/productos" className="bg-image-1 option d-flex justify-content-center align-items-center flex-grow-1 text-decoration-none">
            <section>
              <p>Ver<br/>  Productos</p>
            </section>
          </Link>
          <Link to="/productos?categoria=BEBIDA" className="bg-image-2 option d-flex justify-content-center align-items-center flex-grow-1 text-decoration-none">
            <section>
              <p>Ver <br/> Bebidas</p>
            </section>
          </Link>
        </div>
      </div>

      
      <Footer />
    </main>
    </>
  );  
};

export default Home;