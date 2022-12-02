package com.example.linventario

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.linventario.databinding.FragmentTransactionsBinding
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapterGlobal : TransaccionAdapter
private var eliminar = false
private lateinit var recyclerView: RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment(), TransaccionAdapter.OnProductListener, SearchView.OnQueryTextListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val bind = FragmentTransactionsBinding.inflate(layoutInflater)

        val adaptador = TransaccionAdapter(Transaccion.transaccionsArrayList, this)
        //adapterGlobal = adaptador

        if(Transaccion.transaccionsArrayList.isNotEmpty())
            bind.rvListaTransacciones.adapter = adaptador

        recyclerView = bind.rvListaTransacciones

        bind.btnNewTransaction.setOnClickListener{
            val intent = Intent (activity, newTransactionActivity::class.java)
            startActivity(intent)
        }
        bind.btnDeleteTransaccion.setOnClickListener {
            Toast.makeText(activity, "Seleccione el elemento que desea eliminar", Toast.LENGTH_SHORT).show()
            eliminar = true;
        }

        bind.searchTransacciones.setOnQueryTextListener(this)
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
         * @return A new instance of fragment TransactionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onProductClick(position: Int){
        //TODO: SABER SI AL ELIMINAR VOLVER A LA NORMALIDAD LA CANTIDAD DE PRODUCTO
        if(eliminar){
            val sqLiteManager = SQLiteManager.instanceOfDatabase(activity)
            val transaccion = Transaccion.transaccionsArrayList[position]
            sqLiteManager.deleteTransaccion(transaccion)
            val intent = Intent (activity, MainActivity::class.java)
            intent.putExtra("fromTransaccion", true)
            startActivity(intent)
        }
        eliminar = false
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String): Boolean {
        if(Transaccion.transaccionsArrayList.isNotEmpty()) {
            var listaTransaccionesFiltrada = filtrado(p0)

            if(listaTransaccionesFiltrada.isNotEmpty()) {
                adapterGlobal = TransaccionAdapter(listaTransaccionesFiltrada, this)
            }
            else{
                adapterGlobal = TransaccionAdapter(Transaccion.transaccionsArrayList, this)
                Toast.makeText(activity, "No se encontro ningun elemento con ese nombre", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = adapterGlobal
        }
        else
            Toast.makeText(activity, "No existe ningun producto", Toast.LENGTH_SHORT).show()

        return false
    }

    private fun filtrado(porFiltrar : String) : ArrayList<Transaccion>{
        var lista = ArrayList<Transaccion>()
        if(porFiltrar.isNotEmpty()){
            Transaccion.transaccionsArrayList.forEach{
                if (it.nombreProducto.lowercase(Locale.getDefault()).contains(porFiltrar.lowercase())){
                    lista.add(it)
                }
            }
        }
        return lista
    }
}