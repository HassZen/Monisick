package com.capstone.monisick.ui.history.log

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.monisick.R
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.database.entity.LogEntity
import com.capstone.monisick.databinding.ActivityAddLogBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class AddLogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLogBinding
    private val viewModel: LogViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var currentLog: LogEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val log = intent.getParcelableExtra<LogEntity>(EXTRA_LOG)
        currentLog = log
        if (log != null) {
            binding.todayDate.setText(log.tanggal)
            binding.inputLog.setText(log.content)
            binding.btnAddLog.text = getString(R.string.update_log)
        } else {
            binding.todayDate.setText(getCurrentDate())
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnAddLog.setOnClickListener { saveOrUpdateLog() }
            btnDelete.setOnClickListener { deleteLog() }
            btnCancel.setOnClickListener { finish() }
        }
    }

    private fun saveOrUpdateLog() {
        val todayDate = binding.todayDate.text.toString()
        val logInput = binding.inputLog.text.toString()
        if (logInput.isBlank()) {
            showToast("Silakan masukkan Catatan")
            return
        }
        val log = currentLog?.copy(
            tanggal = todayDate,
            content = logInput
        ) ?: LogEntity(
            id = 0,
            tanggal = todayDate,
            content = logInput
        )
        lifecycleScope.launch {
            if (currentLog == null) {
                viewModel.saveLog(log)
                showToast("Catatan berhasil disimpan")
            } else {
                viewModel.updateLog(log)
                showToast("Catatan berhasil diperbarui")
            }
            finish()
        }
    }

    private fun deleteLog() {
        if (currentLog == null) {
            showToast("Tidak ada catatan untuk dihapus")
            return
        }
        AlertDialog.Builder(this)
            .setTitle("Hapus Catatan")
            .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
            .setPositiveButton("Ya") { dialogInterface, _ ->
                lifecycleScope.launch {
                    viewModel.deleteLog(currentLog!!)
                    showToast("Catatan berhasil dihapus")
                    finish()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_LOG = "extra_log"
    }
}