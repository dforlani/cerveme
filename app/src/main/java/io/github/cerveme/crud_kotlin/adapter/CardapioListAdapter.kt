package io.github.cerveme.crud_kotlin.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import io.github.cerveme.crud_kotlin.R
import io.github.cerveme.crud_kotlin.model.Cardapio
import kotlinx.android.synthetic.main.activity_add_pedido.*
import kotlinx.android.synthetic.main.unidade_cardapio_aux2.*




import java.util.ArrayList

class CardapioListAdapter(private var activity: Activity, private var items: ArrayList<Cardapio>): BaseAdapter() {





    private class ViewHolder(row: View?) {

        var txtTitle: TextView? = null
        var btnPlus: Button? = null
        var btnMinus: Button? = null
        var editQuantidade:EditText? = null

        init {
            this.txtTitle = row?.findViewById(R.id.textDenominacao)
            this.btnPlus = row!!.findViewById(R.id.btnPlus)
            this.btnMinus = row!!.findViewById(R.id.btnMinus)
            this.editQuantidade = row?.findViewById(R.id.editQuantidade)

        }
    }


    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_cardapio, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }


        val cardapio = items[position]
        viewHolder.txtTitle?.text = cardapio.denominacao

        viewHolder.btnPlus?.setOnClickListener {
            var text = viewHolder.editQuantidade
            var i:Int = text?.text.toString().toInt()
            i++
            text?.setText(i.toString())
        }

        viewHolder.btnMinus?.setOnClickListener {
            var text = viewHolder.editQuantidade
            var i:Int = text?.text.toString().toInt()
            i--
            if(i < 0)
                i = 0
            text?.setText(i.toString())
        }



        return view as View
    }

    override fun getItem(i: Int): Cardapio {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return  items[i].pk_preco.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}