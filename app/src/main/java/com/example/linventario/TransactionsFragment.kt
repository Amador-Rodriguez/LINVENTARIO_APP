package com.example.linventario

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.linventario.databinding.FragmentInventoryBinding
import com.example.linventario.databinding.FragmentTransactionsBinding
import kotlinx.android.synthetic.main.fragment_transactions.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapterGlobal : TransaccionAdapter
private var eliminar = false

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment(), TransaccionAdapter.OnProductListener {
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

        bind.btnNewTransaction.setOnClickListener{
            val intent = Intent (activity, newTransactionActivity::class.java)
            startActivity(intent)
        }
        bind.btnDeleteTransaccion.setOnClickListener {
            Toast.makeText(activity, "Seleccione el elemento que desea eliminar", Toast.LENGTH_SHORT).show()
            eliminar = true;
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
}