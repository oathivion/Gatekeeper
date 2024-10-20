package com.example.gatekeeper

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gatekeeper.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    // Store user input as a list of Triple<String, Double, Boolean> where Boolean represents if the goal is completed. Bascially, goal name, point to get, if completed
    private val usergoals = mutableListOf<Triple<String, Double, Boolean>>()
    private var totalValue = 0.0
    private var maxValue = 0.0 // Maximum possible points
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ListView adapter
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf<String>())
        binding.listViewEntries.adapter = adapter

        // Set a listener on the button to add a new goal
        binding.buttonSecond.setOnClickListener {
            showInputDialog()
        }

        binding.listViewEntries.setOnItemClickListener { _, _, position, _ ->
            editCompletionStatus(position)
        }
    }

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter a goal, points, and completion status")

        val inputText = EditText(requireContext()).apply {
            hint = "Enter your goal"
        }
        val inputValue = EditText(requireContext()).apply {
            hint = "Enter points"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL // Numeric input
        }

        // Add Yes/No Buttons for goal completion
        val radioGroup = RadioGroup(requireContext()).apply {
            val yesButton = RadioButton(requireContext()).apply { text = "Yes" }
            val noButton = RadioButton(requireContext()).apply { text = "No" }
            addView(yesButton)
            addView(noButton)
            check(yesButton.id) // Set default to "Yes"
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            addView(inputText)
            addView(inputValue)
            addView(radioGroup)
        }
        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            // Get the user input
            val userGoal = inputText.text.toString()
            val userPoints = inputValue.text.toString().toDoubleOrNull()
            val goalCompleted = radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id // true if "Yes" is selected

            if (userGoal.isNotEmpty() && userPoints != null) {
                // This adds an stuff to the list
                usergoals.add(Triple(userGoal, userPoints, goalCompleted))

                // Updates the max value
                maxValue += userPoints

                // Update the totalValue if the goal is completed
                if (goalCompleted) {
                    totalValue += userPoints
                }

                // Updates the ListView
                updateListView()
                updateTotalTextView()
            } else {
                Toast.makeText(requireContext(), "Please enter valid inputs.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun editCompletionStatus(position: Int) {
        // Create the AlertDialog 
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update goal completion status")

        // Add Yes/No Radio Buttons for goal completion
        val radioGroup = RadioGroup(requireContext()).apply {
            val yesButton = RadioButton(requireContext()).apply { text = "Yes" }
            val noButton = RadioButton(requireContext()).apply { text = "No" }
            addView(yesButton)
            addView(noButton)
            if (usergoals[position].third) check(yesButton.id) else check(noButton.id) // Set to current value
        }

        // Mo Stuff for this app
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            addView(radioGroup)
        }
        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton("OK") { _, _ ->
            // Update the goal completion 
            val goalCompleted = radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id // true if "Yes" is selected

            // Update total values 
            val currentEntry = usergoals[position]
            if (currentEntry.third != goalCompleted) {
                if (goalCompleted) {
                    totalValue += currentEntry.second
                } else {
                    totalValue -= currentEntry.second
                }
             
                usergoals[position] = Triple(currentEntry.first, currentEntry.second, goalCompleted)

                updateListView()
                updateTotalTextView()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show() // Show the dialog
    }

    private fun updateListView() {
        val entryStrings = usergoals.map {
            val status = if (it.third) "Completed" else "Not Completed"
            "${it.first}: ${it.second} points ($status)"
        }
        adapter.clear()
        adapter.addAll(entryStrings)
    }

    private fun updateTotalTextView() {
        val percentageScore = if (maxValue > 0) (totalValue / maxValue) * 100 else 0.0
        binding.textViewTotal.text = "Total: $totalValue / $maxValue (Score: ${percentageScore.toInt()}%)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
