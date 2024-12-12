package com.capstone.monisick.ui.history.obat

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.monisick.R
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.adapter.ObatAdapter
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.data.worker.NotificationUtils
import com.capstone.monisick.databinding.FragmentObatBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class ObatFragment : Fragment() {
    private lateinit var binding: FragmentObatBinding
    private val adapter by lazy {
        ObatAdapter { medication ->
            showDeleteConfirmationDialog(medication)
        }
    }
    private val viewModel: ObatViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            showAddMedicationDialog()
        }
        checkPermissions()
        rvInit()
        observeViewModel()
        reminderNotification()
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissions.toTypedArray(), 1)
        }
    }

    private fun rvInit() {
        binding.rvObat.apply {
            adapter = this@ObatFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.getMeds().observe(viewLifecycleOwner) { meds ->
            adapter.submitList(meds)
            binding.viewEmpty.tvEmpty.text = getString(R.string.no_data)
            binding.viewEmpty.root.visibility =
                if (meds.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun reminderNotification() {
        val switchReminder = binding.switchReminder
        switchReminder.isChecked = true
        viewModel.getNotificationSetting().observe(viewLifecycleOwner) { isNotificationActive ->
            switchReminder.isChecked = isNotificationActive ?: true
        }
        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveNotificationSetting(isChecked)
            if (isChecked) {
                NotificationUtils.schedulePeriodicNotification(requireContext())
            } else {
                NotificationUtils.cancelNotifications(requireContext())
            }
        }
    }

    private fun showAddMedicationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_medication, null)
        val startDateEditText = dialogView.findViewById<EditText>(R.id.startDate)
        val endDateEditText = dialogView.findViewById<EditText>(R.id.endDate)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Simpan", null)
            .setNegativeButton("Batal", null)
            .create()
        dialog.show()
        startDateEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                startDateEditText.setText(selectedDate)
            }
        }
        endDateEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                endDateEditText.setText(selectedDate)
            }
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val medicationName =
                dialogView.findViewById<EditText>(R.id.medication_name).text.toString()
            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()
            if (medicationName.isBlank() || startDate.isBlank() || endDate.isBlank()) {
                Toast.makeText(requireContext(), "Silakan isi semua kolom", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val beforeMeal = dialogView.findViewById<RadioButton>(R.id.before_meal).isChecked
            val morning = dialogView.findViewById<CheckBox>(R.id.morning_checkbox).isChecked
            val afternoon = dialogView.findViewById<CheckBox>(R.id.afternoon_checkbox).isChecked
            val evening = dialogView.findViewById<CheckBox>(R.id.evening_checkbox).isChecked
            val medication = Medication(
                name = medicationName,
                startDate = startDate,
                endDate = endDate,
                beforeMeal = beforeMeal,
                morning = morning,
                afternoon = afternoon,
                evening = evening
            )
            lifecycleScope.launch {
                viewModel.saveMeds(medication)
                dialog.dismiss()
            }
        }
    }

    private fun showDeleteConfirmationDialog(medication: Medication) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Obat")
            .setMessage("Apakah Anda yakin ingin menghapus ${medication.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteMeds(medication)
                    Toast.makeText(
                        requireContext(),
                        "Obat berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}