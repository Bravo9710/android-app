package com.example.yoursavings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    private lateinit var overallBudgetTextView: TextView
    private lateinit var asterisksTextView: TextView

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var showBudgetSwitch: Switch

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        val dataList = readDataFromJson()
        val overallBudget = calculateOverallBudget(dataList)

        overallBudgetTextView = view.findViewById<TextView>(R.id.overallBudget)
        asterisksTextView = view.findViewById<TextView>(R.id.asterisks)
        showBudgetSwitch = view.findViewById<Switch>(R.id.showBudget)

        overallBudgetTextView.text = "$" + String.format("%.2f", overallBudget)

        showBudgetSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If switch is checked, show asterisks
                overallBudgetTextView.visibility = View.VISIBLE
                asterisksTextView.visibility = View.INVISIBLE
            } else {
                // If switch is unchecked, show actual value
//                 // Replace with your actual value
                overallBudgetTextView.visibility = View.INVISIBLE
                asterisksTextView.visibility = View.VISIBLE
            }
        }

        return view
    }

    private fun readDataFromJson(): List<Data> {
        val file = File(requireContext().filesDir, "data.json")
        if (file.exists()) {
            val gson = Gson()
            val listType = object : TypeToken<List<Data>>() {}.type
            return gson.fromJson(FileReader(file), listType)
        }
        return emptyList()
    }

    private fun calculateOverallBudget(dataList: List<Data>): Double {
        var totalIncome = 0.0
        var totalExpenses = 0.0

        for (data in dataList) {
            if (data.income) {
                totalIncome += data.amount ?: 0.0
            } else {
                totalExpenses += data.amount ?: 0.0
            }
        }

        return totalIncome - totalExpenses
    }
}