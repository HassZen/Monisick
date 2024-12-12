package com.capstone.monisick.ui.scan.result

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.database.entity.Makanan
import com.capstone.monisick.data.response.Predictions
import com.capstone.monisick.databinding.FragmentResultBinding
import com.capstone.monisick.ui.scan.ScanViewModel

import java.io.File
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.ui.monitoring.MonitoringDetailActivity
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val navArgs by navArgs<ResultFragmentArgs>()
    private val viewModel: ScanViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        observeSession()
        binding.btnSimpanMakanan.setOnClickListener {
            simpanMakanan()
        }
    }

    private fun observeSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            user.email
        }
    }

    private fun setUpView() {
        val imgResult = navArgs.resultImage
        val imageFile = File(imgResult.imagePath)
        if (imageFile.exists()) {
            Glide.with(requireActivity())
                .load(imageFile)
                .into(binding.imgResult)
            viewModel.uploadPrediction(imageFile)
            observeViewModel()
        } else {
            Toast.makeText(
                requireContext(),
                "Terjadi kesalahan saat memuat gambar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewModel.uploadPredictionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultValue.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultValue.Success -> {
                    binding.progressBar.visibility = View.GONE
                    updateUIWithPrediction(result.data.predictions)
                }

                is ResultValue.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUIWithPrediction(predictions: Predictions?) {
        if (predictions != null) {
            binding.tvMass.text = "${predictions.mass ?: 0.0} g"
            binding.tvProtein.text = "${predictions.protein ?: 0.0} g"
            binding.tvKarbohidrat.text = "${predictions.carbohydrates ?: 0.0} g"
            binding.tvKalori.text = "${predictions.fat ?: 0.0} g"
            binding.tvBerat.text = "${predictions.mass ?: 0.0} g"
        } else {
            Toast.makeText(requireContext(), "Tidak ada prediksi yang tersedia", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun simpanMakanan() {
        val namaMakanan = binding.edtInputMakanan.text.toString().trim()
        if (namaMakanan.isEmpty()) {
            Toast.makeText(requireContext(), "Silakan masukkan nama makanan", Toast.LENGTH_SHORT).show()
            return
        }
        val mass = binding.tvBerat.text.toString().removeSuffix("g").trim().toDoubleOrNull() ?: 0.0
        val fat = binding.tvKalori.text.toString().removeSuffix("g").trim().toDoubleOrNull() ?: 0.0
        val protein = binding.tvProtein.text.toString().removeSuffix("g").trim().toDoubleOrNull() ?: 0.0
        val carbohydrates = binding.tvKarbohidrat.text.toString().removeSuffix("g").trim().toDoubleOrNull() ?: 0.0
        val makanan = Makanan(
            namaMakanan = namaMakanan,
            protein = protein,
            carbo = carbohydrates,
            fats = fat,
            mass = mass
        )
        lifecycleScope.launch {
            try {
                viewModel.saveMakanan(makanan)
                Toast.makeText(requireContext(), "Makanan berhasil disimpan!", Toast.LENGTH_SHORT).show()

                // Navigasi ke MonitoringDetailActivity dengan tab Makan aktif
                val intent = Intent(requireContext(), MonitoringDetailActivity::class.java)
                intent.putExtra("selected_tab", 1) // Indeks tab "Makan"
                startActivity(intent)

                // Tutup fragment ini (opsional jika ini bagian dari stack navigation)
                requireActivity().finish()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal menyimpan makanan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}