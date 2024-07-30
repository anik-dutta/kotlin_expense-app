package com.example.expenseapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {

    private lateinit var tableExpenses: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tableExpenses = findViewById(R.id.table_expenses)

        addDummyExpenses()

        // Check for new expense
        val expenseDetails = intent.getStringExtra("expenseDetails")
        expenseDetails?.let {
            addExpenseToTable(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                true
            }
            R.id.action_add_expense -> {
                val intent = Intent(this, AddExpenseActivity::class.java)
                startActivity(intent)
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


    fun receiveFeedback(feedback: String) {
        Toast.makeText(this, "You have rated us: $feedback", Toast.LENGTH_SHORT).show()
    }

    private fun addExpenseToTable(expenseDetails: String) {
        // Split the expense details string
        val expenseParts = expenseDetails.split("\n")
        if (expenseParts.size < 4) return

        // Create a new table row
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        expenseParts.forEachIndexed { index, detail ->
            val textView = TextView(this)
            // Extract the value part of expense detail
            textView.text = detail.split(": ")[1]
            textView.setPadding(8.dp, 8.dp, 8.dp, 8.dp)
            textView.textSize=18f


            // Set layout parameters to align expenses with headers
            val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = when (index) {
                0 -> 1f  // Date
                1 -> 1f  // Amount
                2 -> 2f  // Description
                3 -> 1f  // Category
                else -> 1f
            }
            textView.layoutParams = layoutParams

            val marginParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            marginParams.setMargins(0, 0, 22.dp, 0)
            textView.layoutParams = marginParams

            // Text wrapping
            textView.maxLines = 2
            textView.ellipsize = android.text.TextUtils.TruncateAt.END

            if (index == 2) {
                textView.maxWidth = 300
                textView.setSingleLine(false)
                textView.maxLines = 3
            }

            tableRow.addView(textView)
        }

        tableExpenses.addView(tableRow)
    }

    private fun addDummyExpenses() {
        val dummyExpenses = listOf(
            "Date: 29/07/2024\nAmount: $50.00\nDescription: Groceries including fruits, vegetables, and dairy products\nCategory: Food",
            "Date: 29/07/2024\nAmount: $20.50\nDescription: Phone Bill\nCategory: Utilities",
            "Date: 29/07/2024\nAmount: $10.75\nDescription: Movie tickets for a weekend show\nCategory: Entertainment"
        )

        dummyExpenses.forEach { expense ->
            addExpenseToTable(expense)
        }
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
