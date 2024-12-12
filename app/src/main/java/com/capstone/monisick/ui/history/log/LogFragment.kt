package com.capstone.monisick.ui.history.log

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.monisick.R
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.adapter.LogAdapter
import com.capstone.monisick.databinding.FragmentLogBinding

class LogFragment : Fragment() {
    private lateinit var binding: FragmentLogBinding
    private val viewModel: LogViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val logAdapter by lazy { LogAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fab.setOnClickListener {
                val intent = Intent(requireContext(), AddLogActivity::class.java)
                startActivity(intent)
            }
        }
        rvInit()
        observeViewModel()
    }

    private fun rvInit() {
        binding.rvLog.apply {
            adapter = logAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeViewModel() {
        viewModel.getLogs().observe(viewLifecycleOwner) { bookmarkedEvent ->
            logAdapter.submitList(bookmarkedEvent)
            binding.viewEmpty.tvEmpty.text = getString(R.string.no_data)
            binding.viewEmpty.root.visibility =
                if (bookmarkedEvent.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }
}