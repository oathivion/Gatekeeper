package com.example.gatekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import com.example.gatekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Set up FAB click listener
        binding.fab.setOnClickListener {
            // Create an AlertDialog with an EditText to capture user input
            val editText = EditText(this)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Input String")
                .setMessage("Enter a string to display on the second page:")
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->
                    // Get the input string from the EditText
                    val userInput = editText.text.toString()

                    // Pass the string to the second activity using an Intent
                    val intent = Intent(this, SecondActivity::class.java)
                    intent.putExtra("USER_INPUT", userInput)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}
