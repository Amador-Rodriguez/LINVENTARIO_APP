package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.linventario.databinding.ActivityLoginActivtyBinding
import kotlinx.android.synthetic.main.activity_login_activty.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginActivtyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sqLiteManager = SQLiteManager.instanceOfDatabase(this)

        btn_login.setOnClickListener{
            if(SQLiteManager.isOnlineNet()) {
                val queue = Volley.newRequestQueue(this)

                val data = HashMap<String?, String?>()

                data["correo"] = tb_emailLogin.text.toString()
                data["pwd"] = tb_pwdLogin.text.toString()


                val datos_toSend = JSONObject(data as Map<String?, String?>)

                val url = "http://192.168.0.7:8080/PSM/login_inc.php"

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, datos_toSend,
                    { response ->
                        try {
                            val msg_server = response.getString("mensaje")
                            val error_server = response.getInt("error")
                            Toast.makeText(this, msg_server, Toast.LENGTH_LONG).show()
                            if (error_server == 0) {
                                val sessionManager = SessionManager.getInstance()
                                val id_user = response.getInt("id_user")
                                val nombreEmpresa = response.getString("name")
                                val email_ = response.getString("mail")
                                sessionManager.setSession(id_user, nombreEmpresa, email_)

                                val usuario = Usuario(nombreEmpresa,email_, tb_pwdLogin.text.toString())

                                if(!sqLiteManager.user_exists(tb_emailLogin.text.toString())){
                                    sqLiteManager.addUser(usuario)
                                }

                                descargarProductos(id_user.toString())



                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("fromNew", false)
                                finish()
                                startActivity(intent)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                ) { error -> error.printStackTrace() }

                HttpsTrustManager.allowAllSSL()
                queue.add(jsonObjectRequest)
            }else{

                if(sqLiteManager.user_exists(tb_emailLogin.text.toString())){
                    if(sqLiteManager.login(tb_emailLogin.text.toString(), tb_pwdLogin.text.toString())){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("fromNew", false)
                        finish()
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(this, "Usuario no existente de forma local, conectate a internet", Toast.LENGTH_LONG).show()
                }



            }
        }

        btn_register.setOnClickListener{
            if(SQLiteManager.isOnlineNet()){
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("fromNew", false)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Para registrarte debes tener conexión a internet", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun descargarProductos(idUsuario: String){

        val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
        val queue = Volley.newRequestQueue(this)

        val objeto = JSONObject()

        objeto.put("id_usuario",idUsuario)

        val data = JSONArray()

        data.put(objeto)



        val url = "http://192.168.0.7:8080/PSM/productoGetAll.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url, data,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val producto = response.getJSONObject(i)

                        val id_producto = producto.getInt("id_producto")
                        val id_usuario = producto.getString("id_usuario")
                        val cantidad = producto.getInt("cantidad")
                        val nombre = producto.getString("nombre")
                        val precio_venta = producto.getDouble("precio_venta")
                        val precio_compra = producto.getDouble("precio_compra")
                        val descripcion = producto.getString("descripcion")

                        val producto_ = Producto(id_producto,cantidad,nombre,precio_venta.toFloat(),precio_compra.toFloat(),descripcion)

                        if(!sqLiteManager.producto_exists(producto_)){
                            sqLiteManager.addProductoFromNube(producto_)

                        }

                    }


                    Toast.makeText(this, "Productos descargados correctamente", Toast.LENGTH_LONG).show()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            error -> error.printStackTrace()
        }

        HttpsTrustManager.allowAllSSL()
        queue.add(jsonArrayRequest)
    }

}