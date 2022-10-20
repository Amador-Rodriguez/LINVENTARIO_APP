package com.example.linventario

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_product.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NewProductActivity : AppCompatActivity() {

    private lateinit var btnDatePicker: Button
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)
        btnDatePicker = findViewById(R.id.dtp_fechaExpira)
        btnGuardar = findViewById(R.id.btn_saveNewProduct)
        var calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(calendar)
        }
        btnDatePicker.setOnClickListener{
            DatePickerDialog(this,datePicker,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        btnGuardar.setOnClickListener{
            val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
            val nombreProducto = tb_nombreProducto.text.toString()
            val codigo = tb_codigo.text.toString()
            val precioVenta = tb_precioVenta.text.toString()
            val precioCompra = tb_precioCompra.text.toString()
            val descripcion = tb_descripcion.text.toString()
            val fecha = getDateFromString("02-20-2023 10:00:00")

            var producto = Producto(
                codigo.toInt(), 15, nombreProducto, precioVenta.toFloat(),
                precioCompra.toFloat(), descripcion, fecha)

            Producto.productoArrayList.add(producto)
            Producto.productoArrayList
            sqLiteManager.addProducto(producto)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateLabel(calendar: Calendar){
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        btnDatePicker.setText(sdf.format(calendar.time))
    }

    private fun getDateFromString(string: String): Date? {
        val dateFormat: DateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")

        return try {
            dateFormat.parse(string)
        } catch (e: ParseException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

}