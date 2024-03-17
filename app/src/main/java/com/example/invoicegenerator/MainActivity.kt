package com.example.invoicegenerator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.invoicegenerator.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Direct reference to the components on the layout that have an ID.
    private lateinit var binding: ActivityMainBinding

    // Refers to the (toolbar, button) on the MainActivity in order to share it with all fragments.
     lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and return the root layout.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Find the BottomNavigation and mainButton on the layout.
        val navView: BottomNavigationView = binding.bottomBarMain.navView
        fab = binding.bottomBarMain.mainFab


        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Set each menu as top level destination.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_invoice, R.id.nav_estimate, R.id.nav_payment, R.id.nav_client, R.id.nav_item
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}