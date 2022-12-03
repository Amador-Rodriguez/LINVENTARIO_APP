package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_new_transaction.*
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap

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
                if((!isEntrada) && productoT.cantidad >= tb_codigo2.text.toString().toInt()){
                    val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
                    val transaccion = makeTransaccion(sqLiteManager)
                    Transaccion.transaccionsArrayList.add(transaccion)
                    sqLiteManager.addTransaccion(transaccion)

                    if(SQLiteManager.isOnlineNet()){
                        val queue = Volley.newRequestQueue(this)

                        val session = SessionManager.getInstance()

                        val data = HashMap<String?, String?>()
                        data["id_producto"] = productoT.codigo.toString()
                        data["id_usuario"] = session.id.toString()
                        data["cantidad"] = productoT.cantidad.toString()
                        data["nombre"] = productoT.nombre_producto.toString()
                        data["precio_venta"] = productoT.precioVenta.toString()
                        data["precio_compra"] = productoT.precioCompra.toString()
                        data["descripcion"] = productoT.descripcion.toString()


                        val datos_toSend = JSONObject(data as Map<String?, String?>)
                        val url = "https://linventario.000webhostapp.com/producto_inc.php"

                        val jsonObjectRequest = JsonObjectRequest(
                            Request.Method.PUT, url, datos_toSend,
                            { response ->
                                try {
                                    val msg_server = response.getString("mensaje")
                                    val error_server = response.getInt("error")


                                    if(error_server == 0){

                                        Toast.makeText(this, "Sincronizado", Toast.LENGTH_LONG).show()
                                        val intent = Intent (this, MainActivity::class.java)
                                        intent.putExtra("fromTransaccion", true)
                                        startActivity(intent)
                                    }else{
                                        Toast.makeText(this, msg_server, Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        ) { error -> error.printStackTrace() }

                        HttpsTrustManager.allowAllSSL()
                        queue.add(jsonObjectRequest)
                    }else{
                        val intent = Intent (this, MainActivity::class.java)
                        intent.putExtra("fromTransaccion", true)
                        startActivity(intent)
                    }



                }
                else
                    Toast.makeText(this, "No hay suficientes productos como para hacer la transaccion", Toast.LENGTH_SHORT).show()

                if(isEntrada){
                    val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
                    val transaccion = makeTransaccion(sqLiteManager)
                    Transaccion.transaccionsArrayList.add(transaccion)
                    sqLiteManager.addTransaccion(transaccion)

                    if(SQLiteManager.isOnlineNet()){
                        val queue = Volley.newRequestQueue(this)

                        val session = SessionManager.getInstance()

                        val data = HashMap<String?, String?>()
                        data["id_producto"] = productoT.codigo.toString()
                        data["id_usuario"] = session.id.toString()
                        data["cantidad"] = productoT.cantidad.toString()
                        data["nombre"] = productoT.nombre_producto.toString()
                        data["precio_venta"] = productoT.precioVenta.toString()
                        data["precio_compra"] = productoT.precioCompra.toString()
                        data["descripcion"] = productoT.descripcion.toString()


                        val datos_toSend = JSONObject(data as Map<String?, String?>)
                        val url = "https://linventario.000webhostapp.com/producto_inc.php"

                        val jsonObjectRequest = JsonObjectRequest(
                            Request.Method.PUT, url, datos_toSend,
                            { response ->
                                try {
                                    val msg_server = response.getString("mensaje")
                                    val error_server = response.getInt("error")


                                    if(error_server == 0){

                                        Toast.makeText(this, "Sincronizado", Toast.LENGTH_LONG).show()
                                        val intent = Intent (this, MainActivity::class.java)
                                        intent.putExtra("fromTransaccion", true)
                                        startActivity(intent)
                                    }else{
                                        Toast.makeText(this, msg_server, Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        ) { error -> error.printStackTrace() }

                        HttpsTrustManager.allowAllSSL()
                        queue.add(jsonObjectRequest)
                    }else{
                        val intent = Intent (this, MainActivity::class.java)
                        intent.putExtra("fromTransaccion", true)
                        startActivity(intent)
                    }




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
        return Transaccion(idTransaccion, productoT.codigo, isEntrada, cantidad, observacion, current)
    }

    override fun onProductClick(position: Int){
        positionProducto = position
        productoT = Producto.productoArrayList[position]
        txt_Producto.setText(productoT.nombre_producto)
    }
}