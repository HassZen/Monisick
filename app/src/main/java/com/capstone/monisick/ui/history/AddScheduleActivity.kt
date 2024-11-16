package com.capstone.monisick.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.monisick.R
import com.capstone.monisick.database.entity.Medication
import com.capstone.monisick.database.entity.Schedule
import com.capstone.monisick.databinding.ActivityAddScheduleBinding
import kotlinx.coroutines.launch
import com.capstone.monisick.database.AppDatabase
import android.app.DatePickerDialog
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import java.util.*

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddScheduleBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database
        db = AppDatabase.getDatabase(this)

        // Menampilkan dialog untuk memilih tanggal mulai dan selesai
        binding.inputPeriod.setOnClickListener {
            // Tampilkan date picker dialog untuk memilih start date dan end date
        }

        binding.addObatButton.setOnClickListener {
            // Membuka form dialog untuk menambah obat
            showAddMedicationDialog()
        }

        // Menambahkan aksi untuk memilih tanggal mulai
        binding.startDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.startDate.setText(selectedDate)
            }
        }

        // Menambahkan aksi untuk memilih tanggal selesai
        binding.endDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                binding.endDate.setText(selectedDate)
            }
        }

        binding.saveButton.setOnClickListener {
            saveSchedule()
        }
    }

    // Fungsi untuk menampilkan DatePickerDialog
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                // Format tanggal yang dipilih menjadi dd/MM/yyyy
                val formattedDate = String.format(
                    "%02d/%02d/%04d",
                    selectedDayOfMonth,
                    selectedMonth + 1,
                    selectedYear
                )
                onDateSelected(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun showAddMedicationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_medication, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(dialogView.windowToken, 0)

                val medicationName =
                    dialogView.findViewById<EditText>(R.id.medication_name).text.toString()
                val medicationQuantity =
                    dialogView.findViewById<EditText>(R.id.medication_quantity).text.toString()
                        .toInt()
                val beforeMeal = dialogView.findViewById<RadioButton>(R.id.before_meal).isChecked
                val morning = dialogView.findViewById<CheckBox>(R.id.morning_checkbox).isChecked
                val afternoon = dialogView.findViewById<CheckBox>(R.id.afternoon_checkbox).isChecked
                val evening = dialogView.findViewById<CheckBox>(R.id.evening_checkbox).isChecked

                val medication = Medication(
                    name = medicationName,
                    quantity = medicationQuantity,
                    beforeMeal = beforeMeal,
                    morning = morning,
                    afternoon = afternoon,
                    evening = evening
                )

                // Simpan medication ke dalam database Room
                lifecycleScope.launch {
                    db.medicationDao().insert(medication)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun saveSchedule() {
        val scheduleName = binding.inputName.text.toString()
        val startDate = binding.startDate.text.toString()
        val endDate = binding.endDate.text.toString()

        val schedule = Schedule(
            name = scheduleName,
            startDate = startDate,
            endDate = endDate
        )

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        // Simpan schedule ke dalam database Room
        lifecycleScope.launch {
            db.scheduleDao().insert(schedule)
        }
    }
}