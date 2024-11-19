package com.capstone.monisick.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.R
import com.capstone.monisick.database.AppDatabase
import com.capstone.monisick.database.entity.Schedule
import com.capstone.monisick.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

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
                // Konfirmasi penghapusan
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // Navigate to AddScheduleActivity
                startActivity(Intent(requireContext(), AddScheduleActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}