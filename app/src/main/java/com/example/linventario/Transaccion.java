package com.example.linventario;

import java.util.ArrayList;
import java.util.Date;

public class Transaccion {
    public static ArrayList<Transaccion> transaccionsArrayList = new ArrayList<>();
    private int idTransaccion;
    private int codigoProducto;
    private boolean isEntrada;
    private int cantidad;
    private String observaciones;
    private Date fecha;

    public Transaccion(int idTransaccion, int codigoProducto, boolean isEntrada, int cantidad, String observaciones, Date fecha) {
        this.idTransaccion = idTransaccion;
        this.codigoProducto = codigoProducto;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}


