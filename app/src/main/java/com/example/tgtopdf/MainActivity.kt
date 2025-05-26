package com.example.tgtopdf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MainActivity : AppCompatActivity() {

    private var parsedContent = ""
    private var backgroundResId = android.R.color.white
    private var pdfReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnImport = findViewById<Button>(R.id.btnImport)
        val btnCreatePDF = findViewById<Button>(R.id.btnCreatePDF)
        val btnPreview = findViewById<Button>(R.id.btnPreview)
        val btnShare = findViewById<Button>(R.id.btnShare)
        val txtStatus = findViewById<TextView>(R.id.txtStatus)

        // Importar archivo .tg
        btnImport.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/xml", "application/octet-stream"))
            }
            startActivityForResult(intent, 1)
        }

        // Crear PDF directamente si hay contenido
        btnCreatePDF.setOnClickListener {
            if (parsedContent.isNotBlank()) {
                generatePdf(parsedContent)
            } else {
                Toast.makeText(this, "Primero importa un archivo .tg", Toast.LENGTH_SHORT).show()
            }
        }

        // Editar contenido
        btnPreview.setOnClickListener {
            if (parsedContent.isNotBlank()) {
                val editIntent = Intent(this, EditActivity::class.java).apply {
                    putExtra("content", parsedContent)
                }
                startActivityForResult(editIntent, 2)
            } else {
                Toast.makeText(this, "No hay contenido para editar", Toast.LENGTH_SHORT).show()
            }
        }

        // Compartir PDF
        btnShare.setOnClickListener {
            if (pdfReady) {
                sharePdf()
            } else {
                Toast.makeText(this, "Genera primero un PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generatePdf(content: String) {
        PdfGenerator(this).generateSimplePdf(content, backgroundResId) { uri ->
            if (uri != null) {
                Toast.makeText(this, "PDF generado", Toast.LENGTH_SHORT).show()
                pdfReady = true
            } else {
                Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sharePdf() {
        val pdfPath = File(getExternalFilesDir(null), "partitura_${System.currentTimeMillis()}.pdf")
        val contentUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", pdfPath)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir PDF"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                1 -> {
                    val uri = data.data
                    TuxGuitarParser(this).parse(uri!!) { content ->
                        parsedContent = content
                        runOnUiThread {
                            txtStatus.text = "Archivo cargado:\n$content"
                            Toast.makeText(this, "Archivo .tg cargado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                2 -> {
                    parsedContent = data.getStringExtra("editedContent") ?: parsedContent
                    txtStatus.text = "Contenido actualizado:\n$ParsedContent"
                }
            }
        }
    }
}
