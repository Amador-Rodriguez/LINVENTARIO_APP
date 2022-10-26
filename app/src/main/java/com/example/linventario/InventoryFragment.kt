package com.example.linventario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.linventario.databinding.FragmentInventoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val MENSAJE_SELECCION = "Seleccione el elemento que quiere "
private lateinit var adapterGlobal: InventoryAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [InventoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InventoryFragment : Fragment(), InventoryAdapter.OnProductListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var action = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = FragmentInventoryBinding.inflate(layoutInflater)

        val adaptador = InventoryAdapter(Producto.productoArrayList, this)
        adapterGlobal = adaptador
        if(Producto.productoArrayList.isNotEmpty()) {
            bind.rvListaProductos.adapter = adaptador
        }

        bind.btnNew.setOnClickListener{
            val intent = Intent (activity, NewProductActivity::class.java)
            intent.putExtra("fromEdit", false)
            startActivity(intent)
        }
        bind.btnDelete.setOnClickListener{
            Toast.makeText(activity, MENSAJE_SELECCION + "eliminar", Toast.LENGTH_SHORT).show()
            action = 1
        }
        bind.btnEdit.setOnClickListener{
            Toast.makeText(activity, MENSAJE_SELECCION + "editar", Toast.LENGTH_SHORT).show()
            action = 2
        }

        // Inflate the layout for this fragment
        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InventoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InventoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onProductClick(position: Int) {
        val sqLiteManager = SQLiteManager.instanceOfDatabase(activity);

        //Eliminar
        if (action == 1){
            sqLiteManager.deleteProducto(position)
            adapterGlobal.update(Producto.productoArrayList)
        }
        //Editar
        if (action == 2){
            val intent = Intent(activity, NewProductActivity::class.java)
            intent.putExtra("position", position)
            startActivity(intent)
        }
        action = 0
    }
}
