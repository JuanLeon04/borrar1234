package com.humanitas.backend.entity;

import com.humanitas.backend.entity.Usuario;
import com.humanitas.backend.entity.DiaSemana;
import java.util.List;

public class ReservasPorDia{
    private DiaSemana dia;
    private List<Usuario> usuarios;

    public ReservasPorDia(DiaSemana dia, List<Usuario> usuarios) {
        this.dia = dia;
        this.usuarios = usuarios;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}