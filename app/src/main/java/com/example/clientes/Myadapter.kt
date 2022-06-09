package com.example.clientes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientes.model.ClienteItem

class Myadapter(val context: Context, val userList: List<ClienteItem> ): RecyclerView.Adapter<Myadapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    var Usernombre: TextView
    var Correo: TextView
    var Telefono: TextView


    init {
        Usernombre = itemView.findViewById(R.id.Campo_Nombres)
        Correo = itemView.findViewById(R.id.Campo_Correo)
        Telefono = itemView.findViewById(R.id.Tel)
    }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.vista, parent , false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.Usernombre.text = userList[position].nombre + " " + userList[position].apellidoP + " " +userList[position].apellidoM
        holder.Correo.text = userList[position].email
        holder.Telefono.text = userList[position].telefono.toString()

        holder.itemView.setOnClickListener{
            val perfil = Intent(context, perfilCliente :: class.java)
            perfil.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            perfil.putExtra("idCliente",userList[position].id)
            context.startActivity(perfil)
        }

    }


    override fun getItemCount(): Int {
        return userList.size
    }
}