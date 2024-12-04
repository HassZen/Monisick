package com.capstone.monisick.ui.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.R
import com.capstone.monisick.database.AppDatabase
import com.capstone.monisick.database.entity.Medication
import com.capstone.monisick.database.entity.Schedule
import com.capstone.monisick.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch
import java.util.Calendar

@Suppress("DEPRECATION")
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private lateinit var db: AppDatabase
    private lateinit var adapter: HistoryAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        db = AppDatabase.getDatabase(requireContext())
        val recyclerView: RecyclerView = binding.recyclerViewHistory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        setHasOptionsMenu(true)
        lifecycleScope.launch {
            val schedules = db.scheduleDao().getAllSchedules().toMutableList()
            adapter = HistoryAdapter(schedules) { schedule ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Jadwal")
                    .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            db.scheduleDao().delete(schedule)
                            val updatedSchedules = db.scheduleDao().getAllSchedules()
                            adapter.updateData(updatedSchedules)
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
            recyclerView.adapter = adapter
        }
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                showAddScheduleDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingInflatedId", "ResourceType")
    private fun showAddScheduleDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_schedule, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Jadwal Pengingat Obat")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val scheduleName = dialogView.findViewById<EditText>(R.id.inputName).text.toString()
            val startDate = dialogView.findViewById<EditText>(R.id.startDate).text.toString()
            val endDate = dialogView.findViewById<EditText>(R.id.endDate).text.toString()
            val medicationName =
                dialogView.findViewById<EditText>(R.id.medication_name).text.toString()
            val medicationQuantity =
                dialogView.findViewById<EditText>(R.id.medication_quantity).text.toString()
                    .toIntOrNull()
            val beforeMeal = dialogView.findViewById<RadioButton>(R.id.before_meal).isChecked
            val morning = dialogView.findViewById<CheckBox>(R.id.morning_checkbox).isChecked
            val afternoon = dialogView.findViewById<CheckBox>(R.id.afternoon_checkbox).isChecked
            val evening = dialogView.findViewById<CheckBox>(R.id.evening_checkbox).isChecked
            var valid = true
            if (scheduleName.isEmpty()) {
                dialogView.findViewById<ImageView>(R.id.error_icon_name).visibility = View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_name).visibility = View.GONE
            }
            if (startDate.isEmpty()) {
                dialogView.findViewById<ImageView>(R.id.error_icon_start_date).visibility =
                    View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_start_date).visibility =
                    View.GONE
            }
            if (endDate.isEmpty()) {
                dialogView.findViewById<ImageView>(R.id.error_icon_end_date).visibility =
                    View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_end_date).visibility = View.GONE
            }
            if (medicationName.isEmpty()) {
                dialogView.findViewById<ImageView>(R.id.error_icon_medication_name).visibility =
                    View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_medication_name).visibility =
                    View.GONE
            }
            if (medicationQuantity == null || medicationQuantity <= 0) {
                dialogView.findViewById<ImageView>(R.id.error_icon_medication_quantity).visibility =
                    View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_medication_quantity).visibility =
                    View.GONE
            }
            if (!beforeMeal && !dialogView.findViewById<RadioButton>(R.id.after_meal).isChecked) {
                dialogView.findViewById<ImageView>(R.id.error_icon_intake).visibility = View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_intake).visibility = View.GONE
            }
            if (!morning && !afternoon && !evening) {
                dialogView.findViewById<ImageView>(R.id.error_icon_time).visibility = View.VISIBLE
                valid = false
            } else {
                dialogView.findViewById<ImageView>(R.id.error_icon_time).visibility = View.GONE
            }
            if (!valid) {
                Toast.makeText(
                    requireContext(),
                    "Silakan isi semua bidang yang wajib diisi.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val schedule = Schedule(name = scheduleName, startDate = startDate, endDate = endDate)
            lifecycleScope.launch {
                db.scheduleDao().insert(schedule)
                val medication = Medication(
                    name = medicationName,
                    quantity = medicationQuantity,
                    beforeMeal = beforeMeal,
                    morning = morning,
                    afternoon = afternoon,
                    evening = evening
                )
                db.medicationDao().insert(medication)
                val updatedSchedules = db.scheduleDao().getAllSchedules().toMutableList()
                adapter.updateData(updatedSchedules)
            }
            dialog.dismiss()
        }
        dialogView.findViewById<EditText>(R.id.startDate).setOnClickListener {
            showDatePickerDialog(dialogView.findViewById(R.id.startDate))
        }
        dialogView.findViewById<EditText>(R.id.endDate).setOnClickListener {
            showDatePickerDialog(dialogView.findViewById(R.id.endDate))
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(dateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                val formattedDate = String.format(
                    "%02d/%02d/%04d",
                    selectedDayOfMonth,
                    selectedMonth + 1,
                    selectedYear
                )
                dateEditText.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}