package com.example.yoursavings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader


/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment3 : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DataAdapter
    private lateinit var file: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank3, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val testButton = view.findViewById<Button>(R.id.exportButton)
        testButton.setOnClickListener {
            openFileWithNotesApp()
        }

        // Read JSON data from file
        file = File(requireContext().filesDir, "data.json")
        val gson = Gson()
        val listType = object : TypeToken<List<Data>>() {}.type
        val dataList: MutableList<Data> = if (file.exists()) {
            gson.fromJson(FileReader(file), object : TypeToken<MutableList<Data>>() {}.type)
        } else {
            mutableListOf()
        }

        // Initialize adapter with the data list and set it to RecyclerView
        adapter = DataAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter

        return view
    }

    private fun openFileWithNotesApp() {
        file = File(requireContext().filesDir, "data.json")
        val fileContent = readFileContent()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, fileContent)
        startActivity(Intent.createChooser(intent, "Изберете приложение за бележки"))
    }

    private fun readFileContent(): String {
        val file = File(requireContext().filesDir, "data.json")
        return if (file.exists()) {
            file.readText()
        } else {
            "Файлът data.json не съществува"
        }
    }
}