package com.capstone.monisick.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.monisick.R
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.adapter.MonitoringAdapter
import com.capstone.monisick.data.response.MonitoringResponse
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.databinding.FragmentHomeBinding
import com.capstone.monisick.ui.auth.login.LoginActivity
import com.capstone.monisick.ui.monitoring.MonitoringDetailActivity

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val monitoringAdapter by lazy {
        MonitoringAdapter { _ ->
            val intent = Intent(requireContext(), MonitoringDetailActivity::class.java)
            val monitoringItem: Parcelable? = intent.getParcelableExtra("monitoring_item")
            intent.putExtra("monitoring_item", monitoringItem)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddMonitoringActivity::class.java)
            startActivity(intent)
        }
        setHasOptionsMenu(true)
        rvInit()
        observeViewModel()
    }

    private fun rvInit() {
        binding.apply {
            rvMonitoring.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvMonitoring.adapter = monitoringAdapter
        }
    }

    private fun observeViewModel() {
        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token.isNotEmpty()) {
                // Token is valid, fetch monitoring data
                mainViewModel.getAllMonitoring().observe(viewLifecycleOwner) { result ->
                    handleMonitoringResult(result)
                }
            } else {
                // Handle missing token
                Toast.makeText(requireContext(), "Anda berhasil logout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleMonitoringResult(result: ResultValue<List<MonitoringResponse>>) {
        when (result) {
            is ResultValue.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.viewError.root.visibility = View.GONE
                binding.rvMonitoring.visibility = View.GONE
            }
            is ResultValue.Success -> {
                binding.progressBar.visibility = View.GONE
                val monitoringData = result.data

                if (monitoringData.isEmpty()) {
                    binding.viewError.root.visibility = View.VISIBLE
                    binding.viewError.tvEmpty.text = getString(R.string.no_data_available)
                    binding.rvMonitoring.visibility = View.GONE
                } else {
                    binding.viewError.root.visibility = View.GONE
                    binding.rvMonitoring.visibility = View.VISIBLE
                    monitoringAdapter.submitList(monitoringData)
                }
            }
            is ResultValue.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.viewError.root.visibility = View.VISIBLE
                binding.rvMonitoring.visibility = View.GONE
                binding.viewError.tvEmpty.text = result.error
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                logoutUser()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}