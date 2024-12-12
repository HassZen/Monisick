package com.capstone.monisick.data.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.monisick.data.database.entity.Makanan
import com.capstone.monisick.databinding.DialogMakananDetailBinding
import com.capstone.monisick.databinding.ItemListMakananBinding
import com.capstone.monisick.ui.history.makan.MakanViewModel

class MakanAdapter(private val viewModel: MakanViewModel) :
    ListAdapter<Makanan, MakanAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemListMakananBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: Makanan, viewModel: MakanViewModel) {
            binding.apply {
                textMakananName.text = data.namaMakanan
                itemView.setOnClickListener {
                    val context = itemView.context
                    val dialogBinding =
                        DialogMakananDetailBinding.inflate(LayoutInflater.from(context))
                    dialogBinding.makananNameDetail.text = data.namaMakanan
                    dialogBinding.makananProteinDetail.text = "Protein: ${data.protein} g"
                    dialogBinding.makananCarbsDetail.text = "Karbohidrat: ${data.carbo} g"
                    dialogBinding.makananFatsDetail.text = "Lemak: ${data.fats} g"
                    dialogBinding.makananMassDetail.text = "Berat: ${data.mass} g"
                    val detailDialog = AlertDialog.Builder(context)
                        .setView(dialogBinding.root)
                        .create()
                    dialogBinding.deleteMakananButton.setOnClickListener {
                        AlertDialog.Builder(context)
                            .setTitle("Hapus Makanan")
                            .setMessage("Apakah Anda yakin ingin menghapus ${data.namaMakanan}?")
                            .setPositiveButton("Ya") { dialogInterface, _ ->
                                viewModel.deleteMakanan(data.id)
                                dialogInterface.dismiss()
                                detailDialog.dismiss()
                                Toast.makeText(context, "Makanan berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Tidak") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .show()
                    }
                    detailDialog.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListMakananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Makanan> =
            object : DiffUtil.ItemCallback<Makanan>() {
                override fun areItemsTheSame(oldItem: Makanan, newItem: Makanan): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: Makanan, newItem: Makanan): Boolean {
                    return oldItem == newItem
                }
            }
    }
}