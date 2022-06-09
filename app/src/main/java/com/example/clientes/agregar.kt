package com.example.clientes

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.clientes.model.Res_Data
import com.example.clientes.model.invalidData
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class agregar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agregar_cliente)


        val btnGuardar = findViewById<Button>(R.id.modificar)

        val inputNombre = findViewById<EditText>(R.id.editTextTextPersonName)
        val inputApellidoP = findViewById<EditText>(R.id.editTextTextPersonName2)
        val inputApellidoM = findViewById<EditText>(R.id.editTextTextPersonName3)
        val inputEmail = findViewById<EditText>(R.id.Rcorreo)
        val inputTelefono = findViewById<EditText>(R.id.phone)
        btnGuardar.setOnClickListener{
            val nombre = inputNombre.text.toString()
            val apellidoP = inputApellidoP.text.toString()
            val apellidoM = inputApellidoM.text.toString()
            val email = inputEmail.text.toString()
            val telefonoConver = inputTelefono.text.toString()
            Data_Json(nombre,apellidoP,apellidoM,email,telefonoConver)
        }
    }

    private fun Data_Json(nombre:String,apellidoP:String,apellidoM: String,email:String,telefono: String) {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Base_URl)
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("nombre", nombre)
        jsonObject.put("apellidoP", apellidoP)
        jsonObject.put("apellidoM", apellidoM)
        jsonObject.put("email", email)
    jsonObject.put("telefono", telefono.toLong())

    // Convert JSONObject to String
    val jsonObjectString = jsonObject.toString()

        d("json",""+jsonObjectString)

    // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val response = service.clienteAdd(requestBody)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                )
                d("Pretty Printed JSON :", prettyJson)

                val jsonResponse = gson.fromJson(prettyJson, Res_Data::class.java)

                if (jsonResponse.status == "203") {
                    val jsonResponseInvalid = gson.fromJson(prettyJson, invalidData::class.java)
                    d("error", jsonResponseInvalid.status);
                    var InvalidJson = ""
                    for (name in jsonResponseInvalid.invalid) {
                        InvalidJson += name + "\n"
                    }
                    //d("error: ",InvalidJson)
                    AlertDialog.Builder(this@agregar).apply {
                        setTitle("Error:")
                        setMessage(InvalidJson)
                            .setNegativeButton(android.R.string.ok,
                                DialogInterface.OnClickListener { dialog, which ->
                                    //botón cancel pulsado
                                })
                            .show()
                    }
                } else {

                    val jsonResponse = gson.fromJson(prettyJson, Res_Data::class.java)

                    d("res: ", jsonResponse.succes + " " + jsonResponse.message)
                    //alerta
                    AlertDialog.Builder(this@agregar).apply {
                        setTitle("Alert")
                        setMessage(jsonResponse.message)
                            .setNegativeButton(android.R.string.ok,
                                DialogInterface.OnClickListener { dialog, which ->
                                    //botón cancel pulsado
                                })
                            .show()
                    }
                }


            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
}