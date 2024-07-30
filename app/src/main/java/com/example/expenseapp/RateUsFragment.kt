package com.example.expenseapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment

class RateUsFragment : DialogFragment(R.layout.fragment_rate_us) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelbt: Button = view.findViewById(R.id.cancelBT)
        val submitbt: Button = view.findViewById(R.id.submitBT)
        val radioGroup: RadioGroup = view.findViewById(R.id.RatingRadioGroup)

        // Initially disable the Submit button
        submitbt.isEnabled = false

        // Enable Submit button when a RadioButton is selected
        radioGroup.setOnCheckedChangeListener { _, _ ->
            submitbt.isEnabled = radioGroup.checkedRadioButtonId != -1
        }

        cancelbt.setOnClickListener {
            dismiss()
        }

        submitbt.setOnClickListener {
            val selectedOptionId: Int = radioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val radioButton: RadioButton = view.findViewById(selectedOptionId)
                val mainActivity: MainActivity = activity as MainActivity
                mainActivity.receiveFeedback(radioButton.text.toString())
                dismiss()
            }
        }
    }
}
