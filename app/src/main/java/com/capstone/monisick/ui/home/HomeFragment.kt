package com.capstone.monisick.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.monisick.R
import com.capstone.monisick.databinding.FragmentHomeBinding
import com.capstone.monisick.ui.auth.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Memastikan fragment bisa menampilkan menu
        setHasOptionsMenu(true)

        return root
    }

    // Menambahkan ikon logout di toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Logout action
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        // Menghapus status login di SharedPreferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)  // Menandakan pengguna sudah logout
        editor.apply()

        // Arahkan pengguna kembali ke LoginActivity
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // Menutup MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}