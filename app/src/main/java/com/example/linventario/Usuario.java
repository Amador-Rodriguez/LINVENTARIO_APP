package com.example.linventario;

public class Usuario {
    public static Usuario usuarioConectado;
    private int idUsuario;
    private String name;
    private String email;
    private String password;

    public Usuario(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Usuario(int idUsuario, String name, String email, String password) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
