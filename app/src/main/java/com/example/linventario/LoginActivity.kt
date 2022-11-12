package com.example.linventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.linventario.databinding.ActivityLoginActivtyBinding
import kotlinx.android.synthetic.main.activity_login_activty.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_login.setOnClickListener{
            val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
            if(!sqLiteManager.user_exists(tb_emailLogin.text.toString())){
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()

            }else{
                if(sqLiteManager.login(tb_emailLogin.text.toString(), tb_pwdLogin.text.toString())){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("fromNew", false)
                    finish()
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("fromNew", false)
            startActivity(intent)
        }

    }
}