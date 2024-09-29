package com.example.ejemplo01segundoparcial250920224
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class CamaraActivity : AppCompatActivity() {
    private lateinit var foto: ImageView
    private lateinit var btnTomarFoto: Button
    private lateinit var btnGuardar: ImageButton
    private var imageBitmap: Bitmap? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        foto = findViewById(R.id.imgFoto)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar el botón "Atrás" si es necesario
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tomar Fotografía" // Puedes personalizar el título
        // Verifica si el permiso de cámara ha sido otorgado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si no se ha otorgado, solicita el permiso
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }

        btnTomarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                responseLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
        btnTomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            responseLauncher.launch(intent)
        }

        btnGuardar.setOnClickListener {
            if (imageBitmap != null) {
                mostrarDialogoNombreArchivo()
            } else {
                Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Manejador de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permiso otorgado, puedes hacer lo que necesitas
            } else {
                Toast.makeText(this, "Permiso de cámara necesario para tomar fotos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            imageBitmap = data?.extras?.get("data") as Bitmap
            foto.setImageBitmap(imageBitmap)
        } else {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoNombreArchivo() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Nombre del archivo")
            .setMessage("Ingrese el nombre para la foto:")
            .setView(editText)
            .setPositiveButton("Guardar") { dialog, which ->
                val nombreArchivo = editText.text.toString()
                if (nombreArchivo.isNotEmpty()) {
                    guardarImagen(nombreArchivo)
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarImagen(nombreArchivo: String) {
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!storageDir.exists()) {
            storageDir.mkdirs() // Crea el directorio si no existe
        }
        val file = File(storageDir, "$nombreArchivo.jpg")
        try {
            val outputStream = FileOutputStream(file)
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Toast.makeText(this, "Imagen guardada en: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
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

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
