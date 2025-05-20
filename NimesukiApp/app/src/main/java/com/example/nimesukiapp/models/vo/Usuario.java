package com.example.nimesukiapp.models.vo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private Integer idUsuario;
    private String nombre;
    private String contrasenha;
    private TipoUsuario tipo = TipoUsuario.USER;

    public enum TipoUsuario {
        ADMIN, USER
    }

    public Usuario(String nombre, String contrasenha) {
        this.idUsuario = null;
        this.nombre = nombre;
        this.contrasenha = contrasenha;
        this.tipo = TipoUsuario.USER;
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

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }
}