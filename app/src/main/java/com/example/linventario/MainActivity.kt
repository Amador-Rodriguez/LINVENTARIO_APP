package com.example.linventario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.linventario.databinding.ActivityMainBinding

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
        sqLiteManager.updateNube(productos_Sincronizar, transacciones_Sincronizar)
    }

}