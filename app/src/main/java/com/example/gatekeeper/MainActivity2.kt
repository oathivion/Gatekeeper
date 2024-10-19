package com.example.gatekeeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gatekeeper.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the input string from the Intent
        val userInput = intent.getStringExtra("USER_INPUT")

        // Display the input string on the second page
        binding.textViewUserInput.text = userInput
    }
}