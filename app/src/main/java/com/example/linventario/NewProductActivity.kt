package com.example.linventario

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class NewProductActivity : AppCompatActivity() {

    private lateinit var btnDatePicker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)
        btnDatePicker = findViewById(R.id.dtp_fechaExpira)
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
    }

    private fun updateLabel(calendar: Calendar){
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        btnDatePicker.setText(sdf.format(calendar.time))
    }

}