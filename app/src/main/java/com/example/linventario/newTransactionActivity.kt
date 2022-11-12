package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_new_transaction.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//TODO: VALIDAR QUE HAYA SUFICIENTE PRODUCTO PARA HACER LA TRANSACCION

class newTransactionActivity : AppCompatActivity(), InventoryAdapter.OnProductListener {
    private lateinit var adapterGlobal: InventoryAdapter
    private lateinit var productoT: Producto
    private var isEntrada = false
    private var positionProducto = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        val adaptador = InventoryAdapter(Producto.productoArrayList, this)
        adapterGlobal = adaptador
        if(Producto.productoArrayList.isNotEmpty()) {
            rv_Productos.adapter = adaptador
        }

        txt_Entrada.setOnClickListener{
            if(isEntrada == false){
                txt_Entrada.setBackgroundResource(R.drawable.shape_green)
                txt_Salida.setBackgroundResource(R.drawable.shape_white2)
                isEntrada = true
            }
        }

        txt_Salida.setOnClickListener {
            if(isEntrada){
                txt_Entrada.setBackgroundResource(R.drawable.shape_white2)
                txt_Salida.setBackgroundResource(R.drawable.shape_green)
                isEntrada = false
            }
        }

        btn_saveNewProduct2.setOnClickListener {
            if(positionProducto != -1){
                if(!isEntrada && productoT.cantidad >= tb_codigo2.text.toString().toInt()){
                    val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
                    val transaccion = makeTransaccion(sqLiteManager)
                    Transaccion.transaccionsArrayList.add(transaccion)
                    sqLiteManager.addTransaccion(transaccion)
                    val intent = Intent (this, MainActivity::class.java)
                    intent.putExtra("fromTransaccion", true)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this, "No hay suficientes productos como para hacer la transaccion", Toast.LENGTH_SHORT).show()

                if(isEntrada){
                    val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
                    val transaccion = makeTransaccion(sqLiteManager)
                    Transaccion.transaccionsArrayList.add(transaccion)
                    sqLiteManager.addTransaccion(transaccion)
                    val intent = Intent (this, MainActivity::class.java)
                    intent.putExtra("fromTransaccion", true)
                    startActivity(intent)
                }
            }
            else
                Toast.makeText(this, "Primero eliga el producto con el que se hara la transaccion", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeTransaccion(sqLiteManager: SQLiteManager) : Transaccion {
        val cantidad = tb_codigo2.text.toString().toInt()
        val observacion = tb_descripcion2.text.toString()
        val idTransaccion = Transaccion.transaccionsArrayList.size + 1
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)
        return Transaccion(idTransaccion, productoT.codigo, isEntrada, cantidad, observacion, sqLiteManager.getDateFromString(current))
    }

    override fun onProductClick(position: Int){
        positionProducto = position
        productoT = Producto.productoArrayList[position]
        txt_Producto.setText(productoT.nombre_producto)
    }
}