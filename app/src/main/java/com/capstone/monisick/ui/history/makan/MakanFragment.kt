package com.capstone.monisick.ui.history.makan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.monisick.R
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.adapter.MakanAdapter
import com.capstone.monisick.databinding.FragmentMakanBinding

class MakanFragment : Fragment() {
    private lateinit var binding: FragmentMakanBinding
    private val viewModel: MakanViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val makanAdapter by lazy { MakanAdapter(viewModel) } // Pass ViewModel to adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvInit()
        observeViewModel()
    }

    private fun rvInit() {
        binding.rvMakan.apply {
            adapter = makanAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeViewModel() {
        viewModel.getMakanan().observe(viewLifecycleOwner) { makananList ->
            makanAdapter.submitList(makananList)
            binding.viewEmpty.tvEmpty.text = getString(R.string.no_data)
            binding.viewEmpty.root.visibility =
                if (makananList.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }
}