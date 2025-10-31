package com.example.sosphone

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sosphone.databinding.ActivityPpalBinding
import java.util.Calendar

/**
 * 1.- Descripción general:
 *     Activity principal de la aplicación.
 *     Gestiona las acciones principales del usuario, incluyendo:
 *     - Llamadas de emergencia.
 *     - Apertura de URLs.
 *     - Reproducción de sonidos o videos aleatorios.
 *     - Creación automática de alarmas.
 *     - Gestión de permisos de llamada.
 *
 * 1.1.- Funcionalidades principales:
 *       - Solicita permisos para realizar llamadas telefónicas.
 *       - Permite configurar el número de emergencia (SOS).
 *       - Ejecuta diferentes acciones mediante botones en la interfaz.
 *
 * 1.2.- Componentes clave:
 *       - requestPermissionLauncher: manejador de permisos peligrosos.
 *       - mainBinding: enlace al layout principal.
 *       - phoneSOS: número de teléfono SOS configurado.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityPpalBinding
    private var phoneSOS: String? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permisionPhone = false

    // -------------------------------------------------------
    // Ciclo de vida del Activity
    // -------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityPpalBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        initEventCall()
    }

    /**
     * 2.- Método onResume():
     *     - Se ejecuta cada vez que la actividad vuelve al frente de la pila.
     *     - Recupera el número SOS enviado desde la actividad de configuración.
     *     - Verifica los permisos de llamada y actualiza el campo de texto.
     */
    override fun onResume() {
        super.onResume()
        permisionPhone = isPermissionCall()
        val stringPhone = getString(R.string.string_phone)
        phoneSOS = intent.getStringExtra(stringPhone)
        mainBinding.txtPhone.setText(phoneSOS)
    }

    /**
     * 3.- Inicialización de componentes y listeners.
     *     - Configura los botones principales.
     *     - Registra el lanzador de permisos.
     *     - Verifica si el permiso de llamada está concedido.
     */
    private fun init() {
        llamarUrl()
        Aleatorio()
        alarma()
        registerLauncher()

        if (!isPermissionCall())
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)

        mainBinding.ivChangePhone.setOnClickListener {
            val nameSharedFich = getString(R.string.name_preferen_shared_fich)
            val nameSharedPhone = getString(R.string.name_shared_phone)
            val sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
            val edit = sharedFich.edit()
            edit.remove(nameSharedPhone)
            edit.apply()

            val intent = Intent(this, ConfActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra("back", true)
            }
            startActivity(intent)
        }
    }

    /**
     * 4.- Registro del lanzador de permisos:
     *     - Prepara la solicitud del permiso CALL_PHONE.
     *     - Gestiona la respuesta del usuario (aceptar/denegar).
     */
    private fun registerLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                permisionPhone = true
            } else {
                Toast.makeText(
                    this,
                    "Necesitas habilitar los permisos",
                    Toast.LENGTH_LONG
                ).show()
                goToConfiguracionApp()
            }
        }
    }

    /**
     * 5.- Inicializa el evento de llamada:
     *     - Comprueba el permiso CALL_PHONE.
     *     - Realiza la llamada o lanza la solicitud de permiso.
     */
    private fun initEventCall() {
        mainBinding.button.setOnClickListener {
            permisionPhone = isPermissionCall()
            if (permisionPhone)
                call()
            else
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    /**
     * 6.- Verifica si el permiso de llamada fue concedido.
     *     - Compatible con versiones anteriores a Android M.
     */
    private fun isPermissionCall(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            true
        else
            isPermissionToUser()
    }

    /** 7.- Comprueba si el permiso CALL_PHONE está concedido por el usuario. */
    private fun isPermissionToUser() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED

    /**
     * 8.- Ejecuta la llamada telefónica.
     *     - Usa un Intent implícito con la acción ACTION_CALL.
     *     - Requiere permiso de llamada concedido.
     */
    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneSOS")
        }
        startActivity(intent)
    }

    /**
     * 9.- Abre una URL en el navegador predeterminado del dispositivo.
     */
    private fun llamarUrl() {
        mainBinding.btnUrl.setOnClickListener {
            val url = "https://theuselessweb.com/"
            Toast.makeText(this, "Abriendo URL", Toast.LENGTH_LONG).show()
            val intentUrl = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intentUrl)
        }
    }

    /**
     * 10.- Ejecuta una acción aleatoria:
     *      - Opción 0: reproduce un sonido local.
     *      - Opción 1: abre un video de YouTube.
     */
    private fun Aleatorio() {
        mainBinding.btnAleatorio.setOnClickListener {
            val opcion = (0..1).random()
            Toast.makeText(
                this,
                "¿Se abrirá YouTube o se reproducirá un sonido?",
                Toast.LENGTH_SHORT
            ).show()

            when (opcion) {
                0 -> {
                    val mediaPlayer = MediaPlayer.create(this, R.raw.sonido1)
                    mediaPlayer.start()
                    Toast.makeText(
                        this,
                        "Reproduciendo sonido de alerta 🔊",
                        Toast.LENGTH_LONG
                    ).show()
                }

                1 -> {
                    val url = "https://youtu.be/s_0wpGrrP5M?si=dV6O4E7iF8Shv0Rz"
                    Toast.makeText(
                        this,
                        "Abriendo contenido multimedia",
                        Toast.LENGTH_LONG
                    ).show()
                    val intentUrlYT = Intent(Intent.ACTION_VIEW, url.toUri())
                    startActivity(intentUrlYT)
                }

                else -> println("Error inesperado en selección aleatoria")
            }
        }
    }

    /**
     * 11.- Crea una alarma automática:
     *       - Configura una alarma para 2 minutos después de la hora actual.
     *       - Usa el Intent ACTION_SET_ALARM.
     */
    private fun alarma() {
        mainBinding.btnAlarma.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.add(Calendar.MINUTE, 2)

            val hora = cal.get(Calendar.HOUR_OF_DAY)
            val minutos = cal.get(Calendar.MINUTE)

            Toast.makeText(
                this,
                "Configurando alarma para $hora:$minutos",
                Toast.LENGTH_SHORT
            ).show()

            val intentAlarma = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma Autogenerada")
                putExtra(AlarmClock.EXTRA_HOUR, hora)
                putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            }

            startActivity(intentAlarma)
            Toast.makeText(this, R.string.textoAlarma, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 12.- Abre la configuración de la aplicación actual:
     *       - Lleva al usuario a la pantalla de ajustes del sistema para esta app.
     */
    private fun goToConfiguracionApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    /**
     * 13.- onNewIntent():
     *       - Gestiona nuevos intents recibidos cuando la actividad ya está en ejecución.
     *       - Permite actualizar los extras sin recrear la actividad.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
