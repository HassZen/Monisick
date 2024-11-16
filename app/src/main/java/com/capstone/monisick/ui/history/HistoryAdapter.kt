package com.capstone.monisick.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.database.entity.Schedule
import com.capstone.monisick.databinding.ItemHistoryBinding

class HistoryAdapter(private val schedules: List<Schedule>) :
    RecyclerView.Adapter<HistoryAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.bind(schedule)
    }

    override fun getItemCount(): Int = schedules.size

    class ScheduleViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(schedule: Schedule) {
            binding.textScheduleName.text = schedule.name
            binding.textSchedulePeriod.text = "From: ${schedule.startDate} To: ${schedule.endDate}"
        }
    }
}