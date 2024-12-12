package com.capstone.monisick.data.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.data.database.entity.LogEntity
import com.capstone.monisick.databinding.ItemListLogBinding
import com.capstone.monisick.ui.history.log.AddLogActivity

class LogAdapter : ListAdapter<LogEntity, LogAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemListLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LogEntity) {
            binding.apply {
                textLogTanggal.text = data.tanggal
                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, AddLogActivity::class.java)
                    intentDetail.putExtra(AddLogActivity.EXTRA_LOG, data)
                    itemView.context.startActivity(intentDetail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<LogEntity> =
            object : DiffUtil.ItemCallback<LogEntity>() {
                override fun areItemsTheSame(oldItem: LogEntity, storyItem: LogEntity): Boolean {
                    return oldItem.id == storyItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: LogEntity, storyItem: LogEntity): Boolean {
                    return oldItem == storyItem
                }
            }
    }
}