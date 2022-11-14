package com.example.linventario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import com.example.linventario.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), TransaccionAdapter.OnProductListener {
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
        val bind = FragmentHomeBinding.inflate(layoutInflater)
        bind.txtCompanyName.text = Usuario.usuarioConectado.name
        var listDashboard = arrayListOf<Transaccion>()
        if (Transaccion.transaccionsArrayList.size > 5){
            for (i in 0..5){
                listDashboard[i] = Transaccion.transaccionsArrayList[i]
            }
        }
        else if(Transaccion.transaccionsArrayList.size > 0){
            listDashboard = Transaccion.transaccionsArrayList
        }

        if(Transaccion.transaccionsArrayList.isNotEmpty()){
            val adaptador = TransaccionAdapter(listDashboard, this)
            bind.rvTransaccionesHome.adapter = adaptador
        }

        if(Producto.productoArrayList.isNotEmpty()){
            var totalProductos = 0
            var bajoStock = 0
            for(producto in Producto.productoArrayList){
                totalProductos += producto.cantidad
                if (producto.cantidad < 10)
                    bajoStock++
            }

            bind.tbTotalProductos.text = totalProductos.toString()
            bind.tbBajoStock.text = bajoStock.toString()
        }

        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onProductClick(position: Int){

    }
}