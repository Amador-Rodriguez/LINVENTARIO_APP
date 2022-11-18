package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.linventario.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_login_activty.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_createAcount.setOnClickListener{
            val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
            val queue = Volley.newRequestQueue(this)

            val data = HashMap<String?, String?>()
            data["nombre"] = tb_companyname.text.toString()
            data["correo"] = tb_email.text.toString()
            data["contrasena"] = tb_password.text.toString()
            data["v_contrasena"] = tb_confirmpassword.text.toString()

            val usuario = Usuario(tb_companyname.text.toString(), tb_email.text.toString(), tb_password.text.toString())


            val datos_toSend = JSONObject(data as Map<String?, String?>)

            val url = "http://192.168.0.7:8080/PSM/register_inc.php"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, datos_toSend,
                { response ->
                    try {
                        val msg_server = response.getString("mensaje")
                        val error_server = response.getInt("error")
                        Toast.makeText(this, msg_server, Toast.LENGTH_LONG).show()
                        if(error_server == 0){
                            sqLiteManager.addUser(usuario)
                            sqLiteManager.login(tb_email.text.toString(), tb_password.text.toString())

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


        }
    }

    private fun validateForm():Boolean{
        var isValid = true

        with(binding){
            if(tbCompanyname.text.isBlank() || tb_email.text.isBlank() || tb_password.text.isBlank() || tb_confirmpassword.text.isBlank() ) {
                isValid = false
                Toast.makeText(
                    this@RegisterActivity,
                    "Rellene todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
                return isValid
            }
        }

        return isValid
    }

    private fun validatePassword() : Boolean {
        var isValid = true
        with(binding) {
            if (tb_password.text.toString() != tbConfirmpassword.text.toString()) {
                isValid = false
                Toast.makeText(
                    this@RegisterActivity,
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()
                return isValid

            }
            if(tb_password.text.length < 8){
                isValid = false
                Toast.makeText(
                    this@RegisterActivity,
                    "La contraseña debe contener al menos 8 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
                return isValid

            }
        }
        return isValid

    }



}