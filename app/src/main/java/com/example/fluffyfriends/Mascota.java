package com.example.fluffyfriends;

import java.io.Serializable;
import java.util.UUID;

public class Mascota implements Serializable {

    private String id;
    private String nombre;
    private String raza;
    private String sexo;
    private String tipo;
    private String color;
    private String descripcion;
    private String observaciones;
    private String fotoPath;

    public Mascota() {
        this.id = UUID.randomUUID().toString();
    }

    public Mascota(String nombre, String raza, String sexo, String tipo, String color, String descripcion, String observaciones, String fotoPath) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.tipo = tipo;
        this.color = color;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.fotoPath = fotoPath;
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

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}
