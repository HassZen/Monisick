package com.capstone.monisick.ui.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.databinding.ActivityAddMonitoringBinding
import java.util.Calendar

class AddMonitoringActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMonitoringBinding
    private val mainViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDatePickers()
        setupSaveButton()
    }

    private fun setupDatePickers() {
        binding.startDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.startDate.setText(selectedDate)
            }
        }
        binding.endDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.endDate.setText(selectedDate)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate =
                String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(formattedDate)
        }, year, month, day).show()
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val monitoringName = binding.inputName.text.toString().trim()
            val startDate = binding.startDate.text.toString().trim()
            val endDate = binding.endDate.text.toString().trim()
            if (monitoringName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addMonitoring(monitoringName, startDate, endDate, status = "active")
        }
    }

    private fun addMonitoring(
        monitoringName: String, startDate: String, endDate: String, @Suppress(
            "SameParameterValue"
        ) status: String
    ) {
        mainViewModel.addMonitoring(monitoringName, startDate, endDate, status)
            .observe(this) { result ->
                when (result) {
                    is ResultValue.Loading -> {
                        Toast.makeText(this, "Menyimpan...", Toast.LENGTH_SHORT).show()
                    }

                    is ResultValue.Success -> {
                        Toast.makeText(this, "Monitoring berhasil ditambahkan!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    is ResultValue.Error -> {
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}