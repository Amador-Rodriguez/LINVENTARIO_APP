package com.example.linventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.linventario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.selectedItemId = R.id.home
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
        val sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateProductsList()
    }

}