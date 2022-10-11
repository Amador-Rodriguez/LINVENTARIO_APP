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

    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "nombre_producto";
    private static final String QUANTITY_FIELD = "cantidad";
    private static final String DATE_FIELD = "fecha_ingreso";

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
                .append("CREATE TABLE")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(QUANTITY_FIELD)
                .append(" INT, ")
                .append(DATE_FIELD)
                .append(" TEXT, ");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addProducto (Producto producto){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, producto.getId());
        contentValues.put(NAME_FIELD, producto.getNombre_producto());
        contentValues.put(QUANTITY_FIELD, producto.getCantidad());
        contentValues.put(DATE_FIELD, getStringFromDate(producto.getFecha_ingreso()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateProductsList(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String nombre = result.getString(2);
                    int cantidad = result.getInt(3);
                    Date fechaIngreso = getDateFromString(result.getString(4));
                    Producto producto = new Producto(id, nombre, cantidad, fechaIngreso);
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
