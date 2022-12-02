package com.example.linventario;

import java.util.ArrayList;
import java.util.Date;

public class Transaccion {
    public static ArrayList<Transaccion> transaccionsArrayList = new ArrayList<>();
    private int idTransaccion;
    private int codigoProducto;
    private String nombreProducto;
    private int idUsuario;
    private boolean isEntrada;
    private int cantidad;
    private String observaciones;
    private String fecha;

    public Transaccion(int idTransaccion, int codigoProducto, boolean isEntrada, int cantidad, String observaciones, String fecha) {
        this.idTransaccion = idTransaccion;
        this.codigoProducto = codigoProducto;
        this.isEntrada = isEntrada;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }

    public Transaccion(int idTransaccion, String NombreProducto, boolean isEntrada, int cantidad, String observaciones, String fecha){
        this.idTransaccion = idTransaccion;
        this.nombreProducto = NombreProducto;
        this.isEntrada = isEntrada;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public boolean isEntrada() {
        return isEntrada;
    }

    public void setEntrada(boolean entrada) {
        isEntrada = entrada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}


