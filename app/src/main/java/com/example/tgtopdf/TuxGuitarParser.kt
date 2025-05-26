package com.example.tgtopdf

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class TuxGuitarParser(private val context: Context) {

    fun parse(uri: Uri, callback: (String) -> Unit) {
        Thread {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                val content = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    if (line!!.contains("<title>") || line!!.contains("<track-count>")) {
                        content.append(line).append("\n")
                    }
                }

                callback(content.toString())
            } catch (e: Exception) {
                callback("Error al leer archivo .tg")
            }
        }.start()
    }
}
