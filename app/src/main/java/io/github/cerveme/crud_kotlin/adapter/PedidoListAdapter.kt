package io.github.cerveme.crud_kotlin.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import io.github.cerveme.crud_kotlin.R
import io.github.cerveme.crud_kotlin.model.Pedido

import java.util.ArrayList

class PedidoListAdapter(private var activity: Activity, private var items: ArrayList<Pedido>): BaseAdapter() {


    private class ViewHolder(row: View?) {

        var txtStatus: TextView? = null
        var posicao:Int = 0

        init {
            this.txtStatus = row?.findViewById(R.id.txtStatus)
        }
    }


    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder


        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_pedido, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }


        val pedido = items[position]
        viewHolder.txtStatus?.text = pedido.status
        viewHolder.posicao = position

        return view as View
    }

    override fun getItem(i: Int): Pedido {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return  items[i].pk_pedido_app.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}