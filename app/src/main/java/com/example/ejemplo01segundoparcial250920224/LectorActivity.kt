package com.example.ejemplo01segundoparcial250920224


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ejemplo01segundoparcial250920224.models.CodigoQR
import com.google.zxing.integration.android.IntentIntegrator

class LectorActivity : AppCompatActivity() {
    //Instancias

    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var txtDescripcion: TextView
    private lateinit var txtCodigo: TextView
    private lateinit var btnEscanear: Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnBuscar: ImageButton
    val QRs = ArrayList<CodigoQR>()

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)
        //Asociar con componente grafico
        codigo = findViewById(R.id.etCodigo)
        descripcion = findViewById(R.id.etDescripcion)
        btnEscanear = findViewById(R.id.btnEscanear)
        btnCapturar = findViewById(R.id.btnCapturar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnBuscar = findViewById(R.id.btnBuscar)
        txtCodigo = findViewById(R.id.txtCodigo)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar el botón "Atrás" si es necesario
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lector de Códigos" // Puedes personalizar el título


        // Eventos
        btnEscanear.setOnClickListener { escanearCodigo() }
        btnCapturar.setOnClickListener {
            if (codigo.text.toString().isEmpty()) {
                codigo.error = "Ingrese un codigo"
            } else {
                capturarDescripcion()
            }
        }
        btnBuscar.setOnClickListener {
            if (codigo.text.toString().isEmpty()) {
                codigo.error = "Ingrese un ID"
            } else {
                val qr = buscarCodigo(codigo.text.toString().toInt())
                if (qr != null) {
                    txtCodigo.setText(qr.codigo)
                    txtDescripcion.setText(qr.descripcion)
                } else {
                    Toast.makeText(this, "No se encontró el código", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnLimpiar.setOnClickListener { limpiar() }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Instanciar para leer codigos
        val intentIntegrator = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // Validar que no este vacia
        if (intentIntegrator != null) {
            // Validar leyo informacion
            if (intentIntegrator.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            } else {
                //Mensaje informativo - si hubo datos
                Toast.makeText(this, "Escaneado: " + intentIntegrator.contents, Toast.LENGTH_SHORT).show()
                //Asignar el resultado al EditText
                codigo.setText(intentIntegrator.contents)
            }// if-else == null
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }// if-else != null
    }

    private fun limpiar() {
        codigo.setText("")
        descripcion.setText("")
    }

    private fun escanearCodigo() {
        //Instanciar para leer codigos
        val intentIntegrator = IntentIntegrator(this@LectorActivity)
        //Definir el tipo de codigo a leer cualquier formato de codigo
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Escanear Codigo") //Mensaje al usuario
        intentIntegrator.setCameraId(0) //Camara a usar
        intentIntegrator.setBeepEnabled(true) //Sonido al escanear
        intentIntegrator.setBarcodeImageEnabled(true) // Guardar imagen
        intentIntegrator.initiateScan() //Iniciar el escaneo

        Toast.makeText(this, "Escanear Codigo", Toast.LENGTH_SHORT).show()

    }

    private fun capturarDescripcion() {
        if (descripcion.text.toString().isEmpty() || codigo.text.toString().isEmpty()) {
            descripcion.error = "Ingrese una descripcion"
            codigo.error = "Ingrese un codigo"
            return
        }else{
            // Crear un nuevo objeto CodigoQR con un ID auto-incrementado
            QRs.add(CodigoQR.create(codigo.text.toString(), descripcion.text.toString()))
            Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
            limpiar()
        }
        Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
        limpiar()
    }

    private fun buscarCodigo(codigo: Int): CodigoQR? {
        for (qr in QRs) {
            if (qr.id ==  codigo) {
                return qr
            }
        }
        return null
    }

    // Manejar la acción del botón "Atrás"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Regresar a la actividad anterior
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}