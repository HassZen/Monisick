package com.capstone.monisick.data.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.databinding.DialogMedicationDetailBinding
import com.capstone.monisick.databinding.ItemHistoryBinding

class ObatAdapter(
    private val onDeleteClicked: (Medication) -> Unit
) : ListAdapter<Medication, ObatAdapter.ScheduleViewHolder>(DIFF_CALLBACK) {
    class ScheduleViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(meds: Medication, onDeleteClicked: (Medication) -> Unit) {
            binding.textScheduleName.text = meds.name
            binding.textSchedulePeriod.text =
                "Jadwal : ${meds.startDate} - ${meds.endDate}"
            itemView.setOnClickListener {
                val context = itemView.context
                val dialogBinding =
                    DialogMedicationDetailBinding.inflate(LayoutInflater.from(context))
                dialogBinding.medicationNameDetail.text = meds.name
                dialogBinding.medicationConsumptionDetail.text =
                    "Dikonsumsi ${if (meds.beforeMeal) "Sebelum Makan" else "Sesudah Makan"}"
                val times = mutableListOf<String>()
                if (meds.morning) times.add("Pagi")
                if (meds.afternoon) times.add("Siang")
                if (meds.evening) times.add("Malam")
                dialogBinding.medicationTimeDetail.text =
                    "Waktu Konsumsi : ${times.joinToString(", ")}"
                dialogBinding.medicationDateRangeDetail.text =
                    "Jadwal : ${meds.startDate} - ${meds.endDate}"
                val dialog = AlertDialog.Builder(context)
                    .setView(dialogBinding.root)
                    .create()
                dialogBinding.deleteMedicationButton.setOnClickListener {
                    onDeleteClicked(meds)
                    dialog.dismiss()
                }
                dialog.show()
            }
            binding.deleteButton.setOnClickListener {
                onDeleteClicked(meds)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClicked)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Medication> =
            object : DiffUtil.ItemCallback<Medication>() {
                override fun areItemsTheSame(oldItem: Medication, newItem: Medication): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Medication, newItem: Medication): Boolean {
                    return oldItem == newItem
                }
            }
    }
}