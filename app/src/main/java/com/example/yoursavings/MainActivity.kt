package com.example.yoursavings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val page1 = BlankFragment()
        val page2 = BlankFragment2()
        val page3 = BlankFragment3()

        changePages(page1)

        val customNavBar = findViewById<BottomNavigationView>(R.id.main_menu)
        customNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_tab -> changePages(page1)
                R.id.add_tab -> changePages(page2)
                R.id.history_tab -> changePages(page3)
            }
            true
        }
    }

    private fun changePages(current: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.menu_holder, current)
            commit()
        }
    }
}