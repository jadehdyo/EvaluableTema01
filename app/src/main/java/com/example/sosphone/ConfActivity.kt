package com.example.sosphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosphone.databinding.ActivityConfBinding
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

/**
 * 1.- Descripción general:
 *     Activity encargada de la configuración inicial de la aplicación .
 *     Permite al usuario introducir y validar un número de teléfono SOS,
 *     el cual se almacenará en las preferencias compartidas del sistema.
 *
 * 1.1.- Funcionalidades principales:
 *       - Solicita al usuario un número de teléfono.
 *       - Valida el formato del número introducido (según país).
 *       - Guarda el número en SharedPreferences.
 *       - Lanza la MainActivity con el número registrado.
 *
 * 1.2.- Componentes clave:
 *       - confBinding: enlace al layout de configuración.
 *       - sharedFich: acceso al fichero de preferencias compartidas.
 *       - nameSharedPhone: clave de almacenamiento del teléfono SOS.
 */
class ConfActivity : AppCompatActivity() {

    private lateinit var confBinding: ActivityConfBinding
    private lateinit var sharedFich: SharedPreferences
    private lateinit var nameSharedPhone: String

    // -------------------------------------------------------
    // Ciclo de vida del Activity
    // -------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confBinding = ActivityConfBinding.inflate(layoutInflater)
        setContentView(confBinding.root)

        initPreferentShared()
        start()
        diceMinigame()
        llamarForm()
    }

    /**
     * 2.- Inicializa las preferencias compartidas:
     *     - Abre o crea el fichero de preferencias privadas.
     *     - Carga el nombre de la clave donde se almacena el teléfono.
     */
    private fun initPreferentShared() {
        val nameSharedFich = getString(R.string.name_preferen_shared_fich)
        this.nameSharedPhone = getString(R.string.name_shared_phone)
        this.sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    /**
     * 3.- Método onResume():
     *     - Detecta si el intent incluye el booleano "back" desde MainActivity.
     *     - Si es así, limpia el campo de texto y muestra un mensaje de aviso.
     */
    override fun onResume() {
        super.onResume()
        val ret = intent.getBooleanExtra("back", false)
        if (ret) {
            confBinding.editPhone.setText("")
            Toast.makeText(this, R.string.msg_new_phone, Toast.LENGTH_LONG).show()
            intent.removeExtra("back")
        }
    }

    /**
     * 4.- Inicializa los controles de la interfaz:
     *     - Comprueba si existe un número guardado y, de ser así, lanza MainActivity.
     *     - Configura el botón de confirmación para validar y guardar el nuevo número.
     */
    private fun start() {
        val sharedPhone: String? = sharedFich.getString(nameSharedPhone, null)

        sharedPhone?.let {
            startMainActivity(it)
        }

        confBinding.btnConf.setOnClickListener {
            val numberPhone = confBinding.editPhone.text.toString()

            when {
                numberPhone.isEmpty() -> {
                    Toast.makeText(this, R.string.msg_empty_phone, Toast.LENGTH_LONG).show()
                }

                !isValidPhoneNumber2(numberPhone, "ES") -> {
                    Toast.makeText(this, R.string.msg_not_valid_phone, Toast.LENGTH_LONG).show()
                }

                else -> {
                    val edit = sharedFich.edit()
                    edit.putString(nameSharedPhone, numberPhone)
                    edit.apply()
                    startMainActivity(numberPhone)
                }
            }
        }
    }

    /**
     * 5.- Lanza MainActivity con el número de teléfono configurado:
     *     - Envía el número mediante putExtra().
     *     - Aplica flags para evitar instancias duplicadas en la pila.
     *
     * 5.1.- Flags utilizados:
     *       - FLAG_ACTIVITY_CLEAR_TOP: elimina instancias intermedias.
     *       - FLAG_ACTIVITY_SINGLE_TOP: reutiliza la actividad si ya está activa.
     *       - Permite actualizar el intent en MainActivity usando onNewIntent().
     */
    private fun startMainActivity(phone: String) {
        val intent = Intent(this@ConfActivity, MainActivity::class.java)
        intent.apply {
            putExtra(getString(R.string.string_phone), phone)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    /**
     * 6.- Valida un número telefónico básico (sin código de país):
     *     - Usa la clase PhoneNumberUtils de Android.
     */
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
    }

    /**
     * 7.- Valida un número telefónico internacional:
     *     - Usa la librería libphonenumber de Google.
     *     - Verifica el formato y validez para el país especificado.
     *     @param phoneNumber Número a validar.
     *     @param countryCode Código ISO del país (por ejemplo, "ES" para España).
     */
    fun isValidPhoneNumber2(phoneNumber: String, countryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            val number = phoneUtil.parse(phoneNumber, countryCode)
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 8.- onNewIntent():
     *     - Gestiona intents recibidos cuando la actividad ya está creada.
     *     - Permite actualizar los extras (por ejemplo, al regresar desde MainActivity).
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }


    private fun diceMinigame() {
        // ⭐️ SOLUCIÓN: Usar el ID correcto de la vista en activity_conf.xml
        confBinding.diceminigame.setOnClickListener { // <-- Usando el nombre del binding: diceminigame

            val intent = Intent(this, DiceMinigame::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }

    private fun llamarForm() {
        // ⭐️ SOLUCIÓN: Usar el ID correcto de la vista en activity_conf.xml
        confBinding.form.setOnClickListener { // <-- Usando el nombre del binding: form

            val intent = Intent(this, FormActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }
}
