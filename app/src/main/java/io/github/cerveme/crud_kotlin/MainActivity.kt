package io.github.cerveme.crud_kotlin


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.github.cerveme.crud_kotlin.adapter.PedidoExpListAdapter
import io.github.cerveme.crud_kotlin.adapter.PedidoListAdapter
import io.github.cerveme.crud_kotlin.model.Pedido
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {


    private var jsonParser: JsonParser? = null
    private var gson: Gson? = null
    private var pedidoList: ArrayList<Pedido> = ArrayList()
    private var adapter: PedidoListAdapter? = null
    private var expAdapter: PedidoExpListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        jsonParser = JsonParser()
        gson = Gson()

        //recebe respostas de outras activities se tiver
        val b = intent.extras
        if (b != null) {
            pedidoList = (b.get("pedidoList") as ArrayList<Pedido>)

            //adapter = PedidoListAdapter(this, pedidoList)
            // listViewPedido.setAdapter(adapter)

            expAdapter = PedidoExpListAdapter(this, pedidoList)
            expListViewPedido.setAdapter(expAdapter)
        }

        btnAddPedido.setOnClickListener({ v ->
            val i = Intent(v.context, ViewPedido::class.java)
            i.putExtra("pedidoList", pedidoList)
            v.context.startActivity(i)
        })


    }

    fun main() {
        println("Start")

        // Start a coroutine
        GlobalScope.launch {
            delay(1000)
            println("Hello")
        }

        Thread.sleep(2000) // wait for 2 seconds
        println("Stop")
    }


}
