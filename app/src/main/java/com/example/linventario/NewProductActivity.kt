package com.example.linventario

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_product.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class NewProductActivity : AppCompatActivity() {
    private lateinit var btnDatePicker: Button
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var positionProducto = intent.getIntExtra("position", -1)

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

        if (positionProducto != -1){
            startElements(positionProducto)
            btnGuardar.setText("Editar")
        }

        btnDatePicker.setOnClickListener{
            DatePickerDialog(this,datePicker,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        btnGuardar.setOnClickListener{
            val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
            val producto = makeProducto(sqLiteManager)

            if (positionProducto == -1){
                Producto.productoArrayList.add(producto)
                sqLiteManager.addProducto(producto)
            }
            else{
                sqLiteManager.editProducto(producto)
            }
            val intent = Intent (this, MainActivity::class.java)
            intent.putExtra("fromNew", true)
            startActivity(intent)
        }
    }

    private fun makeProducto(sqLiteManager: SQLiteManager) : Producto{
        val nombreProducto = tb_nombreProducto.text.toString()
        val codigo = tb_codigo.text.toString()
        val cantidad = tb_cantidad.text.toString()
        val precioVenta = tb_precioVenta.text.toString()
        val precioCompra = tb_precioCompra.text.toString()
        val descripcion = tb_descripcion.text.toString()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        //TODO: SABER SI DE VERDAD VAMOS A USAR EL CAMPO DE QUE SI EXPIRA O NO

        return Producto(codigo.toInt(), cantidad.toInt(), nombreProducto, precioVenta.toFloat(),
            precioCompra.toFloat(), descripcion, sqLiteManager.getDateFromString(current))
    }

    private fun updateLabel(calendar: Calendar){
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        btnDatePicker.setText(sdf.format(calendar.time))
    }

    private fun startElements(positionProducto: Int){
        val campo_codigo: EditText = findViewById(R.id.tb_codigo)
        campo_codigo.isEnabled = false

        val productoEdit = Producto.productoArrayList[positionProducto]
        tb_codigo.setText(productoEdit.codigo.toString())
        tb_cantidad.setText(productoEdit.cantidad.toString())
        tb_nombreProducto.setText(productoEdit.nombre_producto)
        tb_precioCompra.setText(productoEdit.precioCompra.toString())
        tb_precioVenta.setText(productoEdit.precioVenta.toString())
        tb_descripcion.setText(productoEdit.descripcion)
    }
}