package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.linventario.databinding.ActivityLoginActivtyBinding
import kotlinx.android.synthetic.main.activity_login_activty.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginActivtyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Toast.makeText(this, "Para iniciar sesión debes tener conexión a internet", Toast.LENGTH_LONG).show()
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
}