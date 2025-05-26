package com.example.tgtopdf

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {

    private lateinit var txtPreview: TextView
    private var selectedTextColor = "#000000"
    private var selectedBackgroundColor = "#FFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        txtPreview = findViewById(R.id.txtPreview)

        val content = intent.getStringExtra("content") ?: ""
        val editText = findViewById<EditText>(R.id.editTextContent)
        editText.setText(content)

        // Adaptadores para spinners
        val textColorAdapter = ArrayAdapter.createFromResource(
            this, R.array.text_colors, android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val backgroundAdapter = ArrayAdapter.createFromResource(
            this, R.array.background_options, android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        findViewById<Spinner>(R.id.spinnerTextColor).adapter = textColorAdapter
        findViewById<Spinner>(R.id.spinnerBackground).adapter = backgroundAdapter

        // Listeners de cambio de estilo
        findViewById<Spinner>(R.id.spinnerTextColor).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedTextColor = when (position) {
                        0 -> "#000000"
                        1 -> "#0000FF"
                        2 -> "#FF0000"
                        3 -> "#008000"
                        else -> "#000000"
                    }
                    updatePreview(editText.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        findViewById<Spinner>(R.id.spinnerBackground).onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedBackgroundColor = when (position) {
                        0 -> "#FFFFFF"
                        1 -> "#000000"
                        2 -> "#DDDDDD"
                        3 -> "#F5F5DC"
                        else -> "#FFFFFF"
                    }
                    findViewById<View>(R.id.previewContainer).setBackgroundColor(Color.parseColor(selectedBackgroundColor))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePreview(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("editedContent", editText.text.toString())
                putExtra("textColor", selectedTextColor)
                putExtra("backgroundColor", selectedBackgroundColor)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun updatePreview(text: String) {
        txtPreview.text = text
        txtPreview.setTextColor(Color.parseColor(selectedTextColor))
    }
}
