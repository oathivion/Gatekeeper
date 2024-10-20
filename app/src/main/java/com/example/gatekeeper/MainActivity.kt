package com.example.gatekeeper

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.gatekeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            // Show an AlertDialog with an EditText to capture user input
            val editText = EditText(this)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Input String")
                .setMessage("Enter a string to display on the second fragment:")
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->
                    // Get the input string from the EditText
                    val userInput = editText.text.toString()

                    // Find the NavController and navigate to the second fragment, passing the input
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    val bundle = Bundle()
                    bundle.putString("USER_INPUT", userInput)

                    // Navigate to the SecondFragment and pass the user input via arguments
                    navController.navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}
