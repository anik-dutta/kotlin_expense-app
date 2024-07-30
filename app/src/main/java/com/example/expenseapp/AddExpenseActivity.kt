package com.example.expenseapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etDate: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSave: Button
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        etDate = findViewById(R.id.et_date)
        etAmount = findViewById(R.id.et_amount)
        etDescription = findViewById(R.id.et_description)
        spinnerCategory = findViewById(R.id.spinner_category)
        btnSave = findViewById(R.id.btn_save)

        etDate.setOnClickListener {
            showDatePickerDialog(etDate)
        }


        setupSpinner()

        btnSave.setOnClickListener {
            saveExpense()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_add_expense -> {
                true
            }
            R.id.action_rate_us -> {
                val rateUsFragment = RateUsFragment()
                rateUsFragment.show(supportFragmentManager, "RateUsFragment")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.expense_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCategory = null
            }
        }
    }

    private fun saveExpense() {
        val date = etDate.text.toString()
        var amount = etAmount.text.toString()
        val description = etDescription.text.toString()
        val amountDouble = amount.toDouble()
        val formattedAmount = String.format("%.2f", amountDouble)
        amount = "$$formattedAmount"

        if (date.isEmpty() || amount.isEmpty() || description.isEmpty() || selectedCategory == null) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val expenseDetails = """
            Date: $date
            Amount: $amount
            Description: $description
            Category: $selectedCategory
        """.trimIndent()

        // Send the values to MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("expenseDetails", expenseDetails)
        }
        startActivity(intent)

        etDate.text.clear()
        etAmount.text.clear()
        etDescription.text.clear()
        spinnerCategory.setSelection(0)
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                editText.setText(dateFormat.format(selectedDate.time))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
