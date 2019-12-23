package io.github.cerveme.crud_kotlin.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import io.github.cerveme.crud_kotlin.model.Pedido
import java.util.ArrayList
import io.github.cerveme.crud_kotlin.R
import io.github.cerveme.crud_kotlin.model.Cardapio
import kotlinx.android.synthetic.main.pedido_parent_layout.*
import kotlinx.android.synthetic.main.pedido_child_layout.*

class PedidoExpListAdapter(internal var ctx: Context,  private var pedidos: ArrayList<Pedido>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return pedidos.size
    }

    override fun getChildrenCount(parent: Int): Int {
        return pedidos[parent].itens.size
    }

    override fun getGroup(parent: Int): Pedido {

        return pedidos[parent]
    }

    override fun getChild(parent: Int, child: Int): Cardapio {
        return pedidos[parent].itens[child]
    }

    override fun getGroupId(parent: Int): Long {
        return pedidos[parent].pk_pedido_app.toLong()
    }

    override fun getChildId(parent: Int, child: Int): Long {
        return pedidos[parent].itens[child].fk_preco.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(parent: Int, isExpanded: Boolean, convertView: View?, parentview: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.pedido_parent_layout, parentview, false)

        }

        val parent_textvew = convertView!!.findViewById(R.id.parent_txt) as TextView
        parent_textvew.text = pedidos[parent].status
        return convertView
    }

    override fun getChildView(parent: Int, child: Int, isLastChild: Boolean, convertView: View?, parentview: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.pedido_child_layout, parentview, false)

        }

        val child_textvew = convertView!!.findViewById(R.id.child_txt) as TextView
        child_textvew.text = getChild(parent, child).quantidade+" "+getChild(parent, child).denominacao
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    //companion object {
    //    var childList: ArrayList<ArrayList<String>>
    //}
}