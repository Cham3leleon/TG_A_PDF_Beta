package com.example.tgtopdf

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PdfGenerator(private val context: Context) {

    fun generateSimplePdf(content: String, backgroundResId: Int, callback: (Uri?) -> Unit) {
        try {
            val resolver = context.contentResolver
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "partitura_${System.currentTimeMillis()}.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }

            val collection = MediaStore.Files.getContentUri("external")
            val uri = resolver.insert(collection, values)

            val document = android.graphics.pdf.PdfDocument()
            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = document.startPage(pageInfo)

            val canvas = page.canvas

            // Dibujar fondo
            if (backgroundResId != 0) {
                val paint = Paint()
                paint.color = Color.parseColor("#FFFFFF")
                canvas.drawPaint(paint)
            }

            // Dibujar texto
            val textPaint = Paint()
            textPaint.color = Color.BLACK
            textPaint.textSize = 14f
            canvas.drawText("Partitura generada desde archivo .tg", 50f, 50f, textPaint)
            canvas.drawText("Contenido interpretado:", 50f, 70f, textPaint)
            canvas.drawText(content.take(300), 50f, 90f, textPaint)

            document.finishPage(page)

            val outStream = uri?.let { resolver.openOutputStream(it) }
            document.writeTo(outStream)
            document.close()
            outStream?.close()

            callback(uri)
        } catch (e: Exception) {
            callback(null)
        }
    }
}
