import React, {createContext, useState, useContext, useEffect} from "react";
import { BASE_URL } from '../config/apiConfig';

const AuthContext = createContext();

export const AuthProvider = ({children = null}) => {
    if (!children) {
        console.log("AuthProvider no recibió ningún children.");
    }
    const [user, setUser] = useState(() => {
        const storedUser = localStorage.getItem("user");
        return storedUser ? JSON.parse(storedUser) : null;
    });
    const [isAuthenticated, setIsAuthenticated] = useState(() => {
        return localStorage.getItem("isAuthenticated") === "true";
    });

    useEffect(() => {
        if (user) {
            localStorage.setItem("user", JSON.stringify(user));
            localStorage.setItem("isAuthenticated", "true");
        } else {
            localStorage.removeItem("user");
            localStorage.setItem("isAuthenticated", "false");
        }
    }, [user, isAuthenticated]);

    // Cambiado: ahora consulta el backend
    const login = async (correo, contrasena) => {
        try {
            const response = await fetch(
                `http://${BASE_URL}/usuarios/login?correo=${(correo)}&contrasena=${(contrasena)}`
            );
            if (response.ok) {
                const userData = await response.json();
                console.log("Usuario autenticado:", userData);
                setUser(userData);
                setIsAuthenticated(true);
                return true;
            } else {
                setUser(null);
                setIsAuthenticated(false);
                console.error("Usuario o contraseña incorrectos.");
                return false;
            }
        } catch (err) {
            setUser(null);
            setIsAuthenticated(false);
            console.log("Error al autenticar:", err);
            return false;
        }
    };

    const logout = () => {
        setUser(null);
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}
export const useAuth = () => useContext(AuthContext);