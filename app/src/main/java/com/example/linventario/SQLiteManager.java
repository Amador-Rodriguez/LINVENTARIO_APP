package com.example.linventario;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class SQLiteManager extends SQLiteOpenHelper {

    private Context context;
    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "Linventario";
    private static final String TABLE_NAME = "Productos";
    private static final String TABLE_USUARIO = "Usuarios";
    private static final String TABLE_PETICIONES = "Peticiones";

    private static final String ID_USER = "id";
    private static final String COMPANY = "nombre";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private static final String ID_FIELD = "codigo";
    private static final String ID_USUARIO_PRODUCTOS = "idUsuario";
    private static final String CANTIDAD = "cantidad";
    private static final String NAME_FIELD = "nombre_producto";
    private static final String PRECIO_VENTA = "precioVenta";
    private static final String PRECIO_COMPRA = "precioCompra";
    private static final String DESCRIPCION = "descripcion";

    private static final String ESTA_SINCRONIZADO = "isSincronized";


    private static final String ID_OBJECT = "id";
    private static final String TYPE_OBJECT = "tipo";


    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null){
            sqLiteManager = new SQLiteManager(context);
            sqLiteManager.context = context;
        }

        return sqLiteManager;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_USUARIO)
                .append("(")
                .append(ID_USER)
                .append(" INTEGER PRIMARY KEY, ")
                .append(COMPANY)
                .append(" TEXT, ")
                .append(EMAIL)
                .append(" TEXT, ")
                .append(PASSWORD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_PETICIONES)
                .append("(")
                .append(ID_OBJECT)
                .append(" INT, ")
                .append(TYPE_OBJECT)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(CANTIDAD)
                .append(" INT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(PRECIO_VENTA)
                .append(" FLOAT, ")
                .append(PRECIO_COMPRA)
                .append(" FLOAT, ")
                .append(DESCRIPCION)
                .append(" TEXT, ")
                .append(ESTA_SINCRONIZADO)
                .append(" BIT, ")
                .append(ID_USUARIO_PRODUCTOS)
                .append(" INT)");

        sqLiteDatabase.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append("Transacciones")
                .append("(")
                .append("idTransaccion")
                .append(" INT, ")
                .append("codigoProducto")
                .append(" INT, ")
                .append("isEntrada")
                .append(" BIT, ")
                .append("cantidad")
                .append(" INT, ")
                .append("observaciones")
                .append(" TEXT, ")
                .append("fecha")
                .append(" TEXT, ")
                .append(ESTA_SINCRONIZADO)
                .append(" BIT, ")
                .append("idUsuario")
                .append(" INT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }



    public void addUser(Usuario usuario){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COMPANY, usuario.getName());
        contentValues.put(ID_USER, usuario.getIdUsuario());
        contentValues.put(EMAIL, usuario.getEmail());
        contentValues.put(PASSWORD, usuario.getPassword());

        sqLiteDatabase.insert(TABLE_USUARIO, null, contentValues);

    }

    public boolean user_exists(String email){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try(Cursor result = sqLiteDatabase.rawQuery("SELECT email FROM " + "Usuarios WHERE email = ?", new String[] {email})){
            if(result.getCount() != 0){
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean login(String email, String pwd){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try(Cursor result = sqLiteDatabase.rawQuery("SELECT id, nombre FROM " + "Usuarios WHERE email = ? AND password = ?", new String[] {email, pwd})){
            if(result.getCount() == 1){
                result.moveToFirst();
                SessionManager sessionManager = SessionManager.getInstance();
                int id = result.getInt(0);
                String nombreEmpresa = result.getString(1);
                sessionManager.setSession(id, nombreEmpresa, email);

                return true;
            }else{
                return false;
            }
        }
    }

    public void addProducto (Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, producto.getCodigo());
        contentValues.put(ID_USUARIO_PRODUCTOS, sessionManager.getId());
        contentValues.put(CANTIDAD, producto.getCantidad());
        contentValues.put(NAME_FIELD, producto.getNombre_producto());
        contentValues.put(PRECIO_VENTA, producto.getPrecioVenta());
        contentValues.put(PRECIO_COMPRA, producto.getPrecioCompra());
        contentValues.put(DESCRIPCION, producto.getDescripcion());
        contentValues.put(ESTA_SINCRONIZADO, 0);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void addProductoFromNube (Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, producto.getCodigo());
        contentValues.put(ID_USUARIO_PRODUCTOS, sessionManager.getId());
        contentValues.put(CANTIDAD, producto.getCantidad());
        contentValues.put(NAME_FIELD, producto.getNombre_producto());
        contentValues.put(PRECIO_VENTA, producto.getPrecioVenta());
        contentValues.put(PRECIO_COMPRA, producto.getPrecioCompra());
        contentValues.put(DESCRIPCION, producto.getDescripcion());
        contentValues.put(ESTA_SINCRONIZADO, 1);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public boolean producto_exists(Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();
        int id = sessionManager.getId();
        int codigo = producto.getCodigo();
        try(Cursor result = sqLiteDatabase.rawQuery("SELECT idUsuario FROM " + "Productos WHERE idUsuario = ? AND codigo = ?", new String[] {Integer.toString(id),Integer.toString(codigo)})){
            if(result.getCount() != 0){
                return true;
            }else{
                return false;
            }
        }
    }

    //TODO: Eliminar tambien la transaccion del producto
    public void deleteProducto(Integer position){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Producto toDelete = Producto.productoArrayList.get(position);

        sqLiteDatabase.execSQL("DELETE FROM Productos WHERE codigo = " + toDelete.getCodigo());
        populateProductsList();
    }

    public void editProducto(Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CANTIDAD, producto.getCantidad());
        contentValues.put(NAME_FIELD, producto.getNombre_producto());
        contentValues.put(PRECIO_VENTA, producto.getPrecioVenta());
        contentValues.put(PRECIO_COMPRA, producto.getPrecioCompra());
        contentValues.put(DESCRIPCION, producto.getDescripcion());
        //contentValues.put(ESTA_SINCRONIZADO, 1);


        SessionManager sessionManager = SessionManager.getInstance();
        sqLiteDatabase.update(TABLE_NAME, contentValues, "codigo = ? AND idUsuario = ?", new String[]{Integer.toString(producto.getCodigo()), Integer.toString(sessionManager.getId()) });
    }

    public void setSync(Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ESTA_SINCRONIZADO, 1);

        SessionManager sessionManager = SessionManager.getInstance();
        sqLiteDatabase.update(TABLE_NAME, contentValues, "codigo = ? AND idUsuario = ?", new String[]{Integer.toString(producto.getCodigo()), Integer.toString(sessionManager.getId()) });
    }

    public void addTransaccion(Transaccion transaccion){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idTransaccion", transaccion.getIdTransaccion());
        contentValues.put("codigoProducto", transaccion.getCodigoProducto());
        contentValues.put("idUsuario", sessionManager.getId());
        contentValues.put("isEntrada", transaccion.isEntrada());
        contentValues.put("cantidad", transaccion.getCantidad());
        contentValues.put("observaciones", transaccion.getObservaciones());
        contentValues.put(ESTA_SINCRONIZADO, 0);
        contentValues.put("fecha", getStringFromDate(transaccion.getFecha()));

        sqLiteDatabase.insert("Transacciones", null, contentValues);

        int cantidadProductos = 0;
        int nuevaCantidad = 0;

        try(Cursor result = sqLiteDatabase.rawQuery("SELECT cantidad FROM Productos WHERE codigo = " + transaccion.getCodigoProducto(), null)){
            if(result.getCount()!=0){
                result.moveToNext();
                cantidadProductos = result.getInt(0);
            }

        }

        if(!transaccion.isEntrada()){
            nuevaCantidad = cantidadProductos - transaccion.getCantidad();
        }
        else
            nuevaCantidad = cantidadProductos + transaccion.getCantidad();

        contentValues.clear();
        contentValues.put(CANTIDAD, nuevaCantidad);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "codigo = ?", new String[]{Integer.toString(transaccion.getCodigoProducto())});
    }

    public void deleteTransaccion(Transaccion transaccion){
        SQLiteDatabase  sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("Transacciones", "idTransaccion = " + transaccion.getIdTransaccion(), null);
    }

    public ArrayList<Transaccion> populateTransaccionesList(){
        Transaccion.transaccionsArrayList.clear();
        ArrayList<Transaccion> porSincronizar = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();

        try(Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + "Transacciones WHERE idUsuario = " +
                sessionManager.getId(), null)){
            if(result.getCount()!=0){
                while (result.moveToNext()){
                    int id = result.getInt(0);
                    int codigoProducto = result.getInt(1);
                    boolean isEntrada = result.getInt(2) > 0;
                    int cantidad =  result.getInt(3);
                    String observaciones = result.getString(4);
                    boolean sincronizado = result.getInt(5) > 0;
                    Date fecha = getDateFromString(result.getString(6));

                    Transaccion transaccion = new Transaccion(id, codigoProducto, isEntrada, cantidad, observaciones, fecha);
                    Transaccion.transaccionsArrayList.add(transaccion);

                    if(!sincronizado){
                        porSincronizar.add(transaccion);
                    }
                }
            }
        }
        return porSincronizar;
    }

    public ArrayList<Producto> populateProductsList(){
        Producto.productoArrayList.clear();
        ArrayList<Producto> porSincronizar = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        SessionManager sessionManager = SessionManager.getInstance();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE idUsuario = " +
               sessionManager.getId(), null)) {
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    int cantidad = result.getInt(1);
                    String nombre = result.getString(2);
                    float precioVenta = result.getInt(3);
                    float precioCompra = result.getInt(4);
                    String descripcion = result.getString(5);
                    boolean sincronizado = result.getInt(7) > 0;

                    Producto producto = new Producto(id, cantidad, nombre, precioVenta, precioCompra, descripcion);
                    Producto.productoArrayList.add(producto);
                    if(!sincronizado)
                        porSincronizar.add(producto);

                }
            }
        }
        return porSincronizar;
    }

    private String getStringFromDate(Date fecha_ingreso){
        if (fecha_ingreso == null)
            return null;

        return dateFormat.format(fecha_ingreso);
    }

    public Date getDateFromString(String string){
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }


}
