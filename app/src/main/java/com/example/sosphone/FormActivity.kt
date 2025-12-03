package com.example.sosphone

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosphone.databinding.ActivityFormBinding
import java.util.Calendar

class FormActivity : AppCompatActivity() {

    // Instancia del View Binding para acceder a las vistas
    private lateinit var binding: ActivityFormBinding

    // Variables para almacenar la fecha seleccionada
    private var selectedDay: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el View Binding
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar listeners y configuraciones
        setupDatePicker()
        setupSubmitButton()
        atrasForm()
    }

    /**
     * Configura el DatePickerDialog para seleccionar una fecha.
     */
    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH)
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Muestra la fecha actual por defecto en el TextView
        updateDateDisplay()

        binding.btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Guardar la nueva fecha seleccionada
                    selectedYear = year
                    selectedMonth = month + 1 // Los meses van de 0-11, sumamos 1
                    selectedDay = dayOfMonth
                    updateDateDisplay()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    /**
     * Actualiza el TextView con la fecha seleccionada.
     */
    private fun updateDateDisplay() {
        binding.txtDateDisplay.text = getString(
            R.string.date_format,
            selectedDay,
            selectedMonth,
            selectedYear
        )
    }

    /**
     * Configura el botón de "Aceptar" para validar todos los campos del formulario.
     */
    private fun setupSubmitButton() {
        binding.btnAccept.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(this, "Formulario Correcto: Todos los campos válidos.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Formulario Incorrecto: Revisa los campos obligatorios.", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun atrasForm() {
        binding.btnBack.setOnClickListener {

            val intent = Intent(this, ConfActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }

    /**
     * Realiza la validación de todos los campos del formulario.
     * @return true si todos los campos son válidos, false en caso contrario.
     */
    private fun validateForm(): Boolean {
        // 1. Validar Campo de Nombre (no vacío)
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            binding.etName.error = "El nombre no puede estar vacío"
            return false
        }

        // 2. Validar Campo de Texto (no vacío)
        val textContent = binding.etDescription.text.toString().trim()
        if (textContent.isEmpty()) {
            binding.etDescription.error = "El texto no puede estar vacío"
            return false
        }

        // 3. Validar RadioButtons (al menos uno seleccionado)
        val isRadioSelected = binding.rbOption1.isChecked ||
                binding.rbOption2.isChecked ||
                binding.rbOption3.isChecked
        if (!isRadioSelected) {
            Toast.makeText(this, "Debe seleccionar una opción de RadioButton.", Toast.LENGTH_SHORT).show()
            return false
        }

        // 4. Los Checkboxes (opcionales) y el Slide Check (on/off) no requieren validación estricta,
        //    pero si quisieras que el slide esté ON:
        /*
        if (!binding.switchOnOff.isChecked) {
             Toast.makeText(this, "El interruptor debe estar ON.", Toast.LENGTH_SHORT).show()
             return false
        }
        */

        // Si todas las validaciones pasan
        return true
    }
}