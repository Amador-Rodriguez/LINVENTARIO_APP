package com.example.linventario

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_item_producto.view.*

class TransaccionAdapter(private var listTransacciones: MutableList<Transaccion>, private var mOnProductListener: TransaccionAdapter.OnProductListener) :
    RecyclerView.Adapter<TransaccionAdapter.ChatViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

            return ChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_item_producto, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.asignarInformacion(listTransacciones[position] , mOnProductListener)
        }

        override fun getItemCount(): Int = listTransacciones.size

        class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            lateinit var var_onTransaccionListener : OnProductListener

            fun asignarInformacion(transaccion: Transaccion, onProductListener_fun: OnProductListener) {
                itemView.txt_cantidad.text = transaccion.cantidad.toString()
                itemView.txt_nombreProducto.text = transaccion.nombreProducto.toString()
                itemView.txt_fecha.text = transaccion.fecha.toString()
                itemView.txt_Descripcion.text = transaccion.observaciones.toString()


                if(transaccion.isEntrada){
                    itemView.txt_cantidad.setTextColor(Color.parseColor("#00aa00"))
                }
                else{
                    itemView.txt_cantidad.setTextColor(Color.parseColor("#aa0000"))
                }

                this.var_onTransaccionListener = onProductListener_fun
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                var_onTransaccionListener.onProductClick(adapterPosition)
            }
        }

        fun update(newLista: MutableList<Transaccion>){
            listTransacciones = newLista
            notifyDataSetChanged()
        }

        interface OnProductListener{
            fun onProductClick(position: Int)
        }
}