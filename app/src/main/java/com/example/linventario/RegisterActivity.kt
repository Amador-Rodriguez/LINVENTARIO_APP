package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.linventario.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_createAcount.setOnClickListener{
            val sqLiteManager = SQLiteManager.instanceOfDatabase(this)
            var name = tb_companyname.text.toString()
            var email = tb_email.text.toString()
            var pwd = tb_password.text.toString()

            var usuario = Usuario(name,email,pwd)

            if(validateForm() && validatePassword()){
                sqLiteManager.addUser(usuario)
                sqLiteManager.login(email, pwd)

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fromNew", false)
                finish()
                startActivity(intent)
            }
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