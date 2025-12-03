package com.example.sosphone


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.content.Intent // Necesario para Intent
import com.example.sosphone.databinding.ActivityDiceminigameBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class DiceMinigame: AppCompatActivity() {

    private lateinit var binding: ActivityDiceminigameBinding
    private var sum : Int = 0
    private var valorIntroducido: Int = 0

    companion object {
        // Clave para pasar el mensaje a la segunda Activity
        const val EXTRA_MESSAGE = "com.example.sosphone.MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiceminigameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
        getBack()
    }

    private fun initEvent() {
        // Se asume que txtResultado es visible por defecto, o se ocultará temporalmente.
        // Las líneas de ocultación inicial han sido eliminadas.
        binding.txtResultado.text = 0.toString() // Inicializar con un valor por defecto

        binding.imageButton.setOnClickListener{
            // 1. Leer y validar la entrada del usuario
            val inputText = binding.editTextExpectedValue.text.toString()
            valorIntroducido = inputText.toIntOrNull() ?: 0

            if (valorIntroducido !in 3..18) {
                // Si la entrada no es un número o está fuera del rango [3, 18], mostrar error
                Toast.makeText(this, "Introduce un valor entre 3 y 18", Toast.LENGTH_SHORT).show()
                binding.txtResultado.text = "?" // Limpiar visualmente
            } else {
                // Valor válido, comenzar el juego
                game()  //comienza el juego
            }
        }
    }

    // Comienza el juego
    private fun game(){
        sheduleRun() //planificamos las tiradas.
    }

    private fun sheduleRun() {

        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..5){
            schedulerExecutor.schedule(
                {
                    runOnUiThread {
                        throwDadoInTime()
                    }
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS)
        }

        schedulerExecutor.schedule({
            runOnUiThread {
                viewResult() // Llama a la función que lanza la nueva Activity
            }
        },
            msc  * 7.toLong(), TimeUnit.MILLISECONDS)

        schedulerExecutor.shutdown()

    }


    /*
    Método que lanza los tres dados a partir de 3 aleatorios (1-6).
     */
    private fun throwDadoInTime() {
        val numDados = Array(3){ Random.nextInt(1, 7) } // De 1 a 6 (7 es exclusivo)
        val imagViews : Array<ImageView> = arrayOf<ImageView>(
            binding.imagviewDado1,
            binding.imagviewDado2,
            binding.imagviewDado3)

        sum = numDados.sum() //me quedo con la suma actual
        for (i in 0..2) // Recorre 3 dados (índice 0, 1, 2)
            selectView(imagViews[i], numDados[i])

    }


    /*
    Método que dependiendo de la vista, carga una imagen de dado u otro.
     */
    private fun selectView(imgV: ImageView, v: Int) {
        when (v){
            1 -> imgV.setImageResource(R.drawable.dado1)
            2 -> imgV.setImageResource(R.drawable.dado2)
            3 -> imgV.setImageResource(R.drawable.dado3)
            4 -> imgV.setImageResource(R.drawable.dado4)
            5 -> imgV.setImageResource(R.drawable.dado5)
            6 -> imgV.setImageResource(R.drawable.dado6)
        }

    }


    /*
Muestra los resultados, actualiza txtResultado y lanza la Activity NumeroAcertado
SOLO si el resultado coincide con el valor esperado.
*/
    private fun viewResult() {
        val esperado = valorIntroducido // Valor esperado por el usuario

        // 1. Mostrar la suma final en la interfaz de DiceMinigame
        binding.txtResultado.text = sum.toString()

        // 2. Comprobar si acertó
        if (sum == esperado) {
            // --- 🏆 ACERTADO: LANZAR TERCERA ACTIVITY ---

            val resultMessage = "¡Felicidades! Acertaste el $esperado"

            // Crear el Intent y pasar el mensaje
            val intent = Intent(this, NumeroAcertado::class.java).apply {
                // Usamos la clave definida en esta propia Activity
                putExtra(DiceMinigame.EXTRA_MESSAGE, resultMessage)
            }
            startActivity(intent)

            // Opcional: Si quieres que DiceMinigame se cierre después de un acierto:
            // finish()

        } else {
            // --- ❌ FALLASTE: QUEDARSE EN DICEMINIGAME ---

            val message = "Fallaste. La suma era $sum, esperabas $esperado. Inténtalo de nuevo."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            // No se lanza ninguna Activity, la ejecución de viewResult() termina aquí.
            // El usuario se queda en la pantalla para volver a pulsar el botón de juego.
        }
    }

    private fun getBack(){
        binding.atrasGato.setOnClickListener {

            val intent = Intent(this, ConfActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra("back", true)
            }
            startActivity(intent)
        }
    }
}