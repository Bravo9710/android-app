package com.example.yoursavings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

data class Data(
    val id: Int,
    val title: String,
    val notes: String,
    val amount: Double? = null,
    val income: Boolean,
    val date: String
)

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment2 : Fragment() {
    private lateinit var titleInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var addButton: View
    private lateinit var removeButton: View
    private lateinit var submitButton: View

    // Flag variables to track the state of the buttons
    private var isAddButtonClicked = false
    private var isRemoveButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank2, container, false)

        titleInput = view.findViewById(R.id.titleInput)
        notesInput = view.findViewById(R.id.notesInput)
        amountInput = view.findViewById(R.id.amountInput)
        addButton = view.findViewById<ImageButton>(R.id.addFundsButton)
        removeButton = view.findViewById<ImageButton>(R.id.removeFundsButton)
        submitButton = view.findViewById<Button>(R.id.submitButton)

        addButton.setOnClickListener {
            isAddButtonClicked = true
            isRemoveButtonClicked = false
            isIncome = true
            addButton.backgroundTintList =
                resources.getColorStateList(R.color.pink_secondary, null)
            removeButton.backgroundTintList =
                resources.getColorStateList(R.color.transparent, null)
        }

        removeButton.setOnClickListener {
            isAddButtonClicked = false
            isRemoveButtonClicked = true
            isIncome = false
            removeButton.backgroundTintList =
                resources.getColorStateList(R.color.pink_secondary, null)
            addButton.backgroundTintList =
                resources.getColorStateList(R.color.transparent, null)
        }

        submitButton.setOnClickListener {
            // Check if any of the input fields are empty
            if (titleInput.text.isEmpty() || amountInput.text.isEmpty() || (!isAddButtonClicked && !isRemoveButtonClicked)) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val file = File(requireContext().filesDir, "data.json")
            val gson = Gson()
            val listType = object : TypeToken<List<Data>>() {}.type
            val dataList: MutableList<Data> = if (file.exists()) {
                gson.fromJson(FileReader(file), listType)
            } else {
                mutableListOf()
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateAndHour: String = sdf.format(Date())

            // Increase ID
            val maxId = dataList.maxByOrNull { it.id }?.id ?: 0
            val newId = maxId + 1
            val formattedAmount = String.format("%.2f", amountInput.text.toString().toDouble())

            // Create new JSON object
            val newData = Data(
                id = newId,
                title = titleInput.text.toString(),
                notes = notesInput.text.toString(),
                amount = formattedAmount.toDouble(),
                income = isIncome,
                date = currentDateAndHour
            )

            // Add new data to the list
            dataList.add(0, newData)

            // Write updated list back to JSON file
            FileWriter(file).use { writer ->
                gson.toJson(dataList, writer)
            }

            // Clear input fields
            titleInput.setText("")
            notesInput.setText("")
            amountInput.setText("")

            Toast.makeText(requireContext(), "Data added successfully", Toast.LENGTH_SHORT).show()
        }


        return view
    }


    companion object {
        private var isIncome = true
    }
}