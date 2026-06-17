package com.example.fluffyfriends;

import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable {

    private String id; // PK
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String fotoPath;

    // Constructor para el REGISTRO
    public Usuario(String nombre, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.telefono = "";
        this.fotoPath = null;
    }

    // Constructor vacío (SQLite)
    public Usuario() {
        this.id = UUID.randomUUID().toString();
    }

    //getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
