package com.joseantonio.norte.lopez.amediasApp.data.local

import android.content.Context
import java.io.File
import android.util.Log
import java.io.IOException

class FormularioManager(private val context: Context) {

     fun guardar(texto: String,idCliente: Int?) : Boolean{
        if(idCliente != null) {
            val fileName = "satisfaccion_$idCliente.txt"
            try {
                val file = File(context.filesDir, fileName)
                file.writeText(texto)
                return true
            }catch (e : IOException){
                Log.e("FormularioManager.guardar","Error al guardar el formulario",e)
                return false
            }
        }else{
            Log.e("FormularioManager.guardar","No se pudo guardar el formulario id cliente nulo")
            return false
        }
    }

    fun leer(idCliente: Int?): String{
        if(idCliente != null) {
            val fileName = "satisfaccion_$idCliente.txt"
            try {
                val file = File(context.filesDir, fileName)
                if (file.exists()) {
                    return file.readText()
                } else{
                    Log.d("FormularioManager.leer","No se encontro el archivo")
                    return ""
                }
            }catch (e : IOException){
                Log.e("FormularioManager.leer","Error al leer el formulario",e)
                return "No se encontro el archivo"
            }
        }else{
            Log.e("FormularioManager.leer","No se pudo leer  el formulario id cliente nulo")
            return "nose pudo leer el formulario id nulo"
        }
    }

    fun eliminar(idCliente: Int?): Boolean {
        if(idCliente != null) {
            val fileName = "satisfaccion_$idCliente.txt"
            try {
                val file = File(context.filesDir, fileName)
                if (file.exists()) {
                    file.delete()
                    return true
                } else{
                    Log.d("FormularioManager.eliminar","No se encontro el archivo")
                    return false
                }
            }catch (e : IOException){
                Log.e("FormularioManager.eliminar","Error al eliminar el formulario",e)
                return false
            }
        }else{
            Log.e("FormularioManager.eliminar","No se pudo eliminar el formulario id cliente nulo")
            return false
        }
    }
}