package com.example.linventario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_item_producto.view.*

class InventoryAdapter (private var listProductos: MutableList<Producto>, private var mOnProductListener: OnProductListener) :
    RecyclerView.Adapter<InventoryAdapter.ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_producto, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.asignarInformacion(listProductos[position] , mOnProductListener)
    }

    override fun getItemCount(): Int = listProductos.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        lateinit var var_onProductListener : OnProductListener

        fun asignarInformacion(producto: Producto, onProductListener_fun: OnProductListener) {
            itemView.txt_cantidad.text = producto.cantidad.toString()
            itemView.txt_nombreProducto.text = producto.nombre_producto
            itemView.txt_fecha.text = producto.fecha_expiracion.toString()
            itemView.txt_Descripcion.text = producto.descripcion.toString()

            this.var_onProductListener = onProductListener_fun
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            var_onProductListener.onProductClick(adapterPosition)
        }
    }

    fun update(newLista: MutableList<Producto>){
        listProductos = newLista
        notifyDataSetChanged()
    }

    interface OnProductListener{
        fun onProductClick(position: Int)
    }

}