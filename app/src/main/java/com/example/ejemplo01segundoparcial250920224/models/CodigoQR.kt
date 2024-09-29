package com.example.ejemplo01segundoparcial250920224.models

class CodigoQR(val id: Int, val codigo: String, val descripcion: String) {
    companion object {
        private var currentId = 0

        fun create(codigo: String, descripcion: String): CodigoQR {
            currentId++
            return CodigoQR(currentId, codigo, descripcion)
        }
    }
}

