package com.example.sosphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
// Importar la clase de binding generada automáticamente
import com.example.sosphone.databinding.ActivityNumeroAcertadoBinding

class NumeroAcertado : AppCompatActivity() {

    // Declarar la variable para View Binding
    private lateinit var binding: ActivityNumeroAcertadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflar la vista y obtener una instancia de binding
        binding = ActivityNumeroAcertadoBinding.inflate(layoutInflater)

        // 2. Establecer la vista raíz del binding como el contenido de la Activity
        setContentView(binding.root)

        // 3. Obtener el mensaje pasado desde DiceMinigame
        val message = intent.getStringExtra(DiceMinigame.EXTRA_MESSAGE)

        // 4. Buscar el TextView y mostrar el mensaje usando el binding
        binding.textViewResult.text = message

        // 5. Configurar el botón para volver a jugar
        binding.buttonFinish.setOnClickListener {
            // finish() cierra la Activity actual y regresa a la Activity anterior
            finish()
        }
    }
}