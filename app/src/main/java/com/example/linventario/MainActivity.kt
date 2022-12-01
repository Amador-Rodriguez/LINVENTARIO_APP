package com.example.linventario

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.linventario.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = getIntent()
        var fromNew = intent.getBooleanExtra("fromNew", false)
        var fromTransaccion = intent.getBooleanExtra("fromTransaccion", false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(fromNew) {
            replaceFragment(InventoryFragment())
            binding.bottomNavigationView.selectedItemId = R.id.inventory
        }
        else if(fromTransaccion){
            replaceFragment(TransactionsFragment())
            binding.bottomNavigationView.selectedItemId = R.id.transactions
        }
        else {
            replaceFragment(HomeFragment())
            binding.bottomNavigationView.selectedItemId = R.id.home
        }

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home->replaceFragment(HomeFragment())
                R.id.inventory->replaceFragment(InventoryFragment())
                R.id.transactions->replaceFragment(TransactionsFragment())
            }
            true

        }
        loadFromDBtoMemory()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun loadFromDBtoMemory(){
        val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
        var productos_Sincronizar = sqLiteManager.populateProductsList()
        var transacciones_Sincronizar = sqLiteManager.populateTransaccionesList()

        //En esta funcion se manda la lista sin sincronizar
        updateNube(productos_Sincronizar, transacciones_Sincronizar)
    }

     fun borrarProductoNube(producto:Producto ){
        val queue = Volley.newRequestQueue(this)

        val session = SessionManager.getInstance()

        val datos = HashMap<String?, String?>()
        datos["id_producto"] = producto.codigo.toString()
        datos["id_usuario"] = session.id.toString()

        val datos_toSend = JSONObject(datos as Map<String?, String?>)
        val url = "http://192.168.0.7:8080/PSM/producto_inc.php"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PATCH, url, datos_toSend,
            { response ->
                try {
                    val msg_server = response.getString("mensaje")
                    val error_server = response.getInt("error")


                    if(error_server == 0){
                        Toast.makeText(this, "Borrado de la nube", Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this, msg_server, Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {

            Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()

        }

        HttpsTrustManager.allowAllSSL()
        queue.add(jsonObjectRequest)
    }

    //TODO: Sincronizar productos y transacciones en la nube
    private fun updateNube(productos: ArrayList<Producto?>?, transacciones: ArrayList<Transaccion?>?) {
        val sqLiteManager = SQLiteManager.instanceOfDatabase(this)

        if (productos != null) {

            for (i in 0 until productos.size) {
                val queue = Volley.newRequestQueue(this)

                val session = SessionManager.getInstance()

                val data = HashMap<String?, String?>()
                data["id_producto"] = productos[i]?.codigo.toString()
                data["id_usuario"] = session.id.toString()
                data["cantidad"] = productos[i]?.cantidad.toString()
                data["nombre"] = productos[i]?.nombre_producto.toString()
                data["precio_venta"] = productos[i]?.precioVenta.toString()
                data["precio_compra"] = productos[i]?.precioCompra.toString()
                data["descripcion"] = productos[i]?.descripcion.toString()


                val datos_toSend = JSONObject(data as Map<String?, String?>)
                val url = "http://192.168.0.7:8080/PSM/producto_inc.php"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, datos_toSend,
                    { response ->
                        try {
                            val msg_server = response.getString("mensaje")
                            val error_server = response.getInt("error")


                            if(error_server == 0){

                                sqLiteManager.setSync(productos[i])
                                Toast.makeText(this, "Sincronizado productos", Toast.LENGTH_LONG).show()

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

            }

        }
        if (transacciones != null) {

            for (i in 0 until transacciones.size) {
                val queue = Volley.newRequestQueue(this)

                val session = SessionManager.getInstance()

                val data = HashMap<String?, String?>()
                data["idTransaccion"] = transacciones[i]?.idTransaccion.toString()
                data["codigoProducto"] = transacciones[i]?.codigoProducto.toString()
                data["idUsuario"] = session.id.toString()
                data["isEntrada"] = if(transacciones[i]?.isEntrada == true)"1" else "0"
                data["cantidad"] = transacciones[i]?.cantidad.toString()
                data["observaciones"] = transacciones[i]?.observaciones.toString()
                data["fecha"] = transacciones[i]?.fecha.toString()


                val datos_toSend = JSONObject(data as Map<String?, String?>)
                val url = "http://192.168.0.7:8080/PSM/transaction_inc.php"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, datos_toSend,
                    { response ->
                        try {
                            val msg_server = response.getString("mensaje")
                            val error_server = response.getInt("error")


                            if(error_server == 0){

                                sqLiteManager.setSyncTransaccion(transacciones[i])
                                Toast.makeText(this, "Sincronizado transacciones", Toast.LENGTH_LONG).show()

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

            }

        }
    }

}