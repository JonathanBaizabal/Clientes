package com.example.clientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientes.model.ClienteItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val Base_URl = "http://192.168.50.88:8000"

class MainActivity : AppCompatActivity() {

    lateinit var myadapter: Myadapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.clientes)
        findViewById<RecyclerView>(R.id.Rcview).setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.Rcview).layoutManager = linearLayoutManager
        findViewById<RecyclerView>(R.id.Rcview).setHasFixedSize(true)
        getDataCliente()
        //Cambio de vista agregar cliente
        val siguiente = findViewById<Button>(R.id.ad)
        siguiente.setOnClickListener {
            val lis = Intent(this, agregar::class.java)
            startActivity(lis)

        }
    }
    private fun getDataCliente() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Base_URl)
            .build()
            .create(APIService::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<ClienteItem>?> {
            override fun onResponse(
                call: Call<List<ClienteItem>?>,
                response: Response<List<ClienteItem>?>
            ) {
                val responseBody = response.body()!!

                myadapter = Myadapter(baseContext, responseBody)
                myadapter.notifyDataSetChanged()
                findViewById<RecyclerView>(R.id.Rcview).adapter = myadapter

            }

            override fun onFailure(call: Call<List<ClienteItem>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure" + t.message)
            }
        })
    }

    }