package com.example.yoursavings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

class DataAdapter(private val context: Context, private val dataList: MutableList<Data>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewNotes: TextView = itemView.findViewById(R.id.textViewNotes)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val imageViewIncome: ImageView = itemView.findViewById(R.id.imageViewIncome)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        init {
            // Set click listener for delete button
            buttonDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(position)
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this?")
            .setPositiveButton("Yes") { dialog, _ ->
                // Remove item from dataList
                dataList.removeAt(position)
                updateDataFile(dataList)
                // Notify adapter of item removal
                notifyItemRemoved(position)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    // Function to update data.json file
    private fun updateDataFile(dataList: MutableList<Data>) {
        val file = File(context.filesDir, "data.json")
        val gson = Gson()
        FileWriter(file).use { writer ->
            gson.toJson(dataList, writer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.textViewTitle.text = data.title
        holder.textViewNotes.text = data.notes
        holder.textViewAmount.text = "$" + data.amount.toString()
        holder.textViewDate.text = data.date

        // Set image based on income status
        if (data.income) {
            holder.imageViewIncome.setImageResource(R.drawable.plus)
        } else {
            holder.imageViewIncome.setImageResource(R.drawable.minus)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
