package com.example.linventario;
import java.util.ArrayList;
import java.util.Date;

public class Producto {
    public static ArrayList<Producto> productoArrayList = new ArrayList<>();
    private int id;
    private String nombre_producto;
    private int cantidad;
    private Date fecha_ingreso;

    public Producto(int id, String nombre_producto, int cantidad, Date fecha_ingreso) {
        this.id = id;
        this.nombre_producto = nombre_producto;
        this.cantidad = cantidad;
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}
