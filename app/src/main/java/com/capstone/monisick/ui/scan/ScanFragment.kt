package com.capstone.monisick.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.monisick.databinding.FragmentScanBinding
import com.capstone.monisick.ui.scan.result.ScanActivity

@Suppress("DEPRECATION")
class ScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnStartScan.setOnClickListener {
                val intent = Intent(requireContext(), ScanActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Kembali dari Scan", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_SCAN = 1001
    }
}