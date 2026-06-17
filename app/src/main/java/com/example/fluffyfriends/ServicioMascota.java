package com.example.fluffyfriends;

import java.io.Serializable;

public class ServicioMascota implements Serializable {
    private int id; // PK autoincrement
    private String mascotaId; // FK de Mascota.id
    private String tipo;
    private String descripcion;
    private String fecha;
    private double precio;
    private String estado; // PENDIENTE_PAGO | ACTIVO | CANCELADO

    // Constructor sin id
    public ServicioMascota(String tipo, String descripcion, String fecha, double precio) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.precio = precio;
        this.estado = "PENDIENTE_PAGO";
    }

    // Constructor vacío
    public ServicioMascota() {

    }

    //getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(String mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
