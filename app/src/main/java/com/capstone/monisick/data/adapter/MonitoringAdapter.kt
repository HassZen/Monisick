package com.capstone.monisick.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.data.response.MonitoringResponse
import com.capstone.monisick.data.utils.formatDate
import com.capstone.monisick.databinding.ItemMonitoringBinding

class MonitoringAdapter(private val onItemClick: (MonitoringResponse) -> Unit) :
    ListAdapter<MonitoringResponse, MonitoringAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(
        private val binding: ItemMonitoringBinding,
        private val onItemClick: (MonitoringResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: MonitoringResponse) {
            binding.apply {
                tvTitleMonitoring.text = data.monitoringName
                tvPeriode.text = "${formatDate(data.startDate)} - ${formatDate(data.endDate)}"
                itemView.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMonitoringBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MonitoringResponse> =
            object : DiffUtil.ItemCallback<MonitoringResponse>() {
                override fun areItemsTheSame(
                    oldItem: MonitoringResponse,
                    storyItem: MonitoringResponse
                ): Boolean {
                    return oldItem.monitoringName == storyItem.monitoringName
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: MonitoringResponse,
                    storyItem: MonitoringResponse
                ): Boolean {
                    return oldItem == storyItem
                }
            }
    }
}