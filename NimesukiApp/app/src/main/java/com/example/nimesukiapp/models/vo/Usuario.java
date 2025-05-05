package com.example.nimesukiapp.models.vo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario;
    private String nombre;
    private String contrasenha;
    private String tipo;

    public Usuario(int idUsuario, String nombre, String contrasenha, String tipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contrasenha = contrasenha;
        this.tipo = tipo;
    }

    public Usuario (String nombre, String contrasenha) {
        this.nombre = nombre;
        this.contrasenha = contrasenha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}