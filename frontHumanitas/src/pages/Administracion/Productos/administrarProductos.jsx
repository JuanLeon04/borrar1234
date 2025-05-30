import React, { useEffect, useState } from "react";
import Header from "../../../componentes/Header/header.jsx";
import Footer from "../../../componentes/Footer/footer.jsx";
import { useAuth } from "../../../context/AuthContext";
import { BASE_URL } from '../../../config/apiConfig';

const AdministrarProductos = () => {

  const { user, isAuthenticated } = useAuth();

  const [nombreTemp, setNombreTemp] = useState("");
  const [categoriaTemp, setCategoriaTemp] = useState("");
  const [precioTemp, setPrecioTemp] = useState("");
  const [imgTemp, setImgTemp] = useState("");
  const [productoSeleccionado, setProductoSeleccionado] = useState(null);
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    fetch(`http://${BASE_URL}/productos`)
      .then((res) => res.json())
      .then((data) => setProductos(data))
      .catch((err) => console.error("Error al cargar productos:", err));
  }, []);

  console.log("Productos:", productos);


  const [showModalModificarProducto, setShowModalModificarProducto] = useState(false); 
  // Abre el modal
  const abrirModal = (p) => {
            setNombreTemp(p.nombre);
            setCategoriaTemp(p.categoria);
            setPrecioTemp(p.precio);
            setImgTemp(p.imagen);

            setProductoSeleccionado(p);
            setShowModalModificarProducto(true);
  };


  // Eliminar un producto
  const handleEliminar = async (p) => {
    const confirmar = window.confirm(`¿Estás seguro de eliminar el producto "${p.nombre}"?`);
    if (!confirmar) return;
    try {
      const response = await fetch(`http://${BASE_URL}/productos/${p.id}?idUsuario=${user.id}`, { // ---------------------------------------------- API
        method: "DELETE"
      });
      if (response.ok) {
        alert(`Producto "${p.nombre}" eliminado correctamente.`);
        window.location.reload(); // Recargar paǵina para ver reflejados los cambios
      } else {
        console.error("Error al eliminar producto");
        alert("No se pudo eliminar el producto.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("Error de conexión con el servidor.");
    }
  };


  // Modificar Poducto
  const handleModificar = async (p) => {
    if (nombreTemp === "" || categoriaTemp ==="" || precioTemp === "" || imgTemp === "") {
      alert(`¡Algunos campos están vacíos!`);
      return;
    }
    const productoModificado = {
      categoria: categoriaTemp,
      imagen: imgTemp,
      nombre: nombreTemp,
      precio: parseFloat(precioTemp),
      
    };
    try {
      const response = await fetch(`http://${BASE_URL}/productos/${p.id}?idUsuario=${user.id}`, { // --------------------------------------------------- API
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(productoModificado)
      });
      if (response.ok) {
        alert("Producto modificado correctamente.");
        setShowModalModificarProducto(false);
        //limpiar campos
        setNombreTemp("");
        setCategoriaTemp("");
        setPrecioTemp("");
        setImgTemp("");
        // ---------------------------
        window.location.reload(); // Recargar paǵina para ver reflejados los cambios
      } else {
        console.error("Error al modificar el producto");
        alert("Error al modificar el producto.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("No se pudo conectar con el servidor.");
    }
  };

// Añadir productos
  const handleAñadir = async (e) => {
    e.preventDefault();
    const nuevoProducto = {
      categoria: categoriaTemp,
      imagen: imgTemp,
      nombre: nombreTemp,
      precio: parseFloat(precioTemp),
      
    };
    console.log("Nuevo producto a añadir:", nuevoProducto);
    try {
      const response = await fetch(`http://${BASE_URL}/productos?idUsuario=${user.id}`, { // ------------------------------------------------- API
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(nuevoProducto)
      });
      if (response.ok) {
        alert("Producto añadido correctamente.");
        e.target.reset(); // limpiar el form
        //limpiar campos
        setNombreTemp("");
        setCategoriaTemp("");
        setPrecioTemp("");
        setImgTemp("");
        // ---------------------------
        window.location.reload(); // Recargar paǵina para ver reflejados los cambios
      } else {
        console.error("Error al añadir producto");
        alert("Hubo un error al añadir el producto.");
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("No se pudo conectar con el servidor.");
    }
  };


  return (
    <>
      <Header />
      <div className="container mt-4">
        <h1 className="mb-4">Administrar Productos</h1>

        <form className="mb-4" onSubmit={handleAñadir}>
          <div className="row">
            <div className="col">
              <input
                type="text"
                className="form-control"
                placeholder="Nombre"
                onChange={(e) => setNombreTemp(e.target.value)}
                required
              />
            </div>
            <div className="col">
              <select className="form-select"
                onChange={(e) => setCategoriaTemp(e.target.value)}
                required
              >
                <option value="" disabled selected>Selecciona categoría</option>
                <option value="COMIDA">COMIDA</option>
                <option value="BEBIDA">BEBIDA</option>
              </select>

            </div>
            <div className="col">
              <input
                type="number"
                step="0.01"
                className="form-control"
                placeholder="Precio"
                onChange={(e) => setPrecioTemp(e.target.value)}
                required
              />
            </div>
            <div className="col">
              <input
                type="text"
                className="form-control"
                placeholder="URL"
                onChange={(e) => setImgTemp(e.target.value)}
                required
              />
            </div>
            <div className="col">
              <button className="btn btn-primary w-100" type="submit">Añadir</button>
            </div>
          </div>
        </form>

        <table className="table table-striped">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Categoría</th>
              <th>Precio</th>
              <th>Imágen</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {productos.map((p) => (
              <tr key={p.id}>
                <td>{p.nombre}</td>
                <td>{p.categoria}</td>
                <td>${p.precio}</td>
                <td><img className="img-fluid rounded" alt={`producto: ${p.nombre}`} src={p.imagen} width={"100px"}/></td>
                <td>
                  <button
                    className="btn btn-primary btn-sm me-2"
                    onClick={() => abrirModal(p)}
                  >
                    Modificar
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleEliminar(p)}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>



        {/* Modal */}
        {showModalModificarProducto && (
            <>
              <div className="modal d-block show" tabIndex="-1" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
                <div className="modal-dialog">
                  <div className="modal-content">
                    <div className="modal-header">
                      <h5 className="modal-title">Modificar producto{" "}<strong>{nombreTemp}</strong></h5>
                      <button type="button" className="btn-close" onClick={() => setShowModalModificarProducto(false)}></button>
                    </div>
                    <div className="modal-body text-center">
                    
                    <label className="form-label">Nombre</label>
                    <input type="text" className="form-control" value={nombreTemp} onChange={(e) => setNombreTemp(e.target.value)} required/>

                    <label className="form-label pt-4">Categoría</label>
                    <select className="form-select" value={categoriaTemp} onChange={(e) => setCategoriaTemp(e.target.value)} required>
                      <option value="" disabled selected>Selecciona categoría</option>
                      <option value="COMIDA">COMIDA</option>
                      <option value="BEBIDA">BEBIDA</option>
                    </select>

                    <label className="form-label pt-4">Precio</label>
                    <input type="number" className="form-control" value={precioTemp} onChange={(e) => setPrecioTemp(e.target.value)} required/>

                    <label className="form-label pt-4">Imágen del producto (URL)</label>
                    <input type="text" className="form-control" value={imgTemp} onChange={(e) => setImgTemp(e.target.value)} required accept=".png, .jpg, .jpeg"/>

                    </div>
                    <div className="modal-footer">
                      <button type="button" className="btn btn-primary" onClick={() => handleModificar(productoSeleccionado)}>¡Modificar!</button>
                      <button type="button" className="btn btn-danger" onClick={() => setShowModalModificarProducto(false)}>Cancelar</button>
                    </div>
                  </div>
                </div>
              </div>
            </>
          )}
      </div>
      <Footer />
    </>
  );
};

export default AdministrarProductos;
