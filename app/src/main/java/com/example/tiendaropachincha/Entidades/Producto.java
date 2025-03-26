package com.example.tiendaropachincha.Entidades;

public class Producto {
    private int id;
    private String tipo;
    private String genero;
    private String talla;
    private double precio;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(int id, String tipo, String genero, String talla, double precio) {
        this.id = id;
        this.tipo = tipo;
        this.genero = genero;
        this.talla = talla;
        this.precio = precio;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return tipo + " (" + talla + ") - S/." + precio;
    }
}