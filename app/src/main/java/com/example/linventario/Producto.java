package com.example.linventario;
import java.util.ArrayList;
import java.util.Date;

public class Producto {
    public static ArrayList<Producto> productoArrayList = new ArrayList<>();
    private int codigo;
    private int cantidad;
    private String nombre_producto;
    private float precioVenta;
    private float precioCompra;
    private String descripcion;
    private Date fecha_expiracion;

    //Con fecha de expiracion
    public Producto(int codigo, int cantidad, String nombre_producto, float precioVenta, float precioCompra, String descripcion, Date FechaExpiracion) {
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.nombre_producto = nombre_producto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.descripcion = descripcion;
        this.fecha_expiracion = FechaExpiracion;
    }

    //Sin fecha de expiracion
    public Producto(int codigo, int cantidad, String nombre_producto, float precioVenta, float precioCompra, String descripcion) {
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.nombre_producto = nombre_producto;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(Date fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }
}
