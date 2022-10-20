package com.example.linventario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "Linventario";
    private static final String TABLE_NAME = "Productos";
    private static final String COUNTER = "counter";

    private static final String ID_FIELD = "codigo";
    private static final String CANTIDAD = "cantidad";
    private static final String NAME_FIELD = "nombre_producto";
    private static final String PRECIO_VENTA = "precioVenta";
    private static final String PRECIO_COMPRA = "precioCompra";
    private static final String DESCRIPCION = "descripcion";
    private static final String EXPIRATION_DATE = "fecha_expiracion";

    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");


    public SQLiteManager(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
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
                .append(EXPIRATION_DATE)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addProducto (Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, producto.getCodigo());
        contentValues.put(CANTIDAD, producto.getCantidad());
        contentValues.put(NAME_FIELD, producto.getNombre_producto());
        contentValues.put(PRECIO_VENTA, producto.getPrecioVenta());
        contentValues.put(PRECIO_COMPRA, producto.getPrecioCompra());
        contentValues.put(DESCRIPCION, producto.getDescripcion());
        contentValues.put(EXPIRATION_DATE, getStringFromDate(producto.getFecha_expiracion()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateProductsList(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    int cantidad = result.getInt(1);
                    String nombre = result.getString(2);
                    float precioVenta = result.getInt(3);
                    float precioCompra = result.getInt(4);
                    String descripcion = result.getString(5);
                    Date fechaExpiracion = getDateFromString(result.getString(6));
                    Producto producto = new Producto(id, cantidad, nombre, precioVenta, precioCompra, descripcion, fechaExpiracion);
                    Producto.productoArrayList.add(producto);
                }
            }
        }
    }

    private String getStringFromDate(Date fecha_ingreso){
        if (fecha_ingreso == null)
            return null;

        return dateFormat.format(fecha_ingreso);
    }

    private Date getDateFromString(String string){
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
