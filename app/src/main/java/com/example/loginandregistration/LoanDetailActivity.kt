package com.example.loginandregistration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.LoanDetailActivity.Companion.EXTRA_LOAN
import com.example.loginandregistration.databinding.ActivityLoanDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class LoanDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoanDetailBinding
    var loanIsEditable = false
    var cal = Calendar.getInstance()
    lateinit var loan : Loan

    companion object {
        val EXTRA_LOAN = "loan"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        binding.buttonDate1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@LoanDetailActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        if(intent.getParcelableExtra<Loan>(EXTRA_LOAN) == null) {
          loan = Loan()
            toggleEditable()
        } else {
            loan = intent.getParcelableExtra<Loan>(EXTRA_LOAN) ?: Loan()
            binding.checkBoxLoanDetailIsFullyRepaid.isChecked = loan.isRepaid
            binding.editTextLoanDetailInitialLoan.setText(loan.debt.toString())
            binding.editTextLoanDetailBorrower.setText(loan.lendee)
            binding.editTextLoanDetailAmountRepaid.setText(loan.amountRepaid.toString())
            binding.editTextLoanDetailReason.setText(loan.reason)
            binding.textViewLoanDetailAmountStillOwed.text = "Still Owed: ${loan.debt - loan.amountRepaid}"
            cal.time = loan.dateLoaned
            updateDateInView()
        }

        binding.buttonLoanDetailSave.setOnClickListener {
            loan.lendee = binding.editTextLoanDetailBorrower.text.toString()
            loan.debt = binding.editTextLoanDetailInitialLoan.text.toString().toInt()
            loan.amountRepaid = binding.editTextLoanDetailAmountRepaid.text.toString().toInt()
            loan.isRepaid = binding.checkBoxLoanDetailIsFullyRepaid.isChecked
            loan.reason = binding.editTextLoanDetailReason.text.toString()
            if(loan.ownerId.isNullOrBlank()){
                loan.ownerId = intent.getStringExtra(LoanListActivity.EXTRA_USER_ID)!!
            }
            updateLoan()
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textViewDate1.text = "Date Loaned: " + sdf.format(cal.getTime())
        loan.dateLoaned = cal.getTime()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_loan_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_item_loan_detail_edit -> {
                toggleEditable()
                true
            }
            R.id.menu_item_loan_detail_delete -> {
                deleteFromBackendless()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFromBackendless() {
        Backendless.Data.of(Loan::class.java).remove(loan,
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    // Person has been deleted. The response is the
                    // time in milliseconds when the object was deleted
                    Toast.makeText(this@LoanDetailActivity, "${loan.lendee} Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d("BirthdayDetail", "handleFault: ${fault.message}")
                }
            })
    }


    private fun toggleEditable() {
        if (loanIsEditable) {
            loanIsEditable = false
            binding.buttonLoanDetailSave.isEnabled = false
            binding.buttonLoanDetailSave.visibility = View.GONE
            binding.buttonDate1.visibility = View.GONE
            binding.textViewDate1.visibility = View.GONE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = false
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailBorrower.isEnabled = false
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailAmountRepaid.isEnabled = false
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailInitialLoan.isEnabled = false
            binding.editTextLoanDetailReason.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailReason.isEnabled = false
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = false
        } else {
            loanIsEditable = true
            binding.buttonLoanDetailSave.isEnabled = true
            binding.buttonLoanDetailSave.visibility = View.VISIBLE
            binding.buttonDate1.visibility = View.VISIBLE
            binding.textViewDate1.visibility = View.VISIBLE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = true
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.editTextLoanDetailBorrower.isEnabled = true
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailAmountRepaid.isEnabled = true
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailInitialLoan.isEnabled = true
            binding.editTextLoanDetailReason.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailReason.isEnabled = true
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = true
        }
    }
    fun updateLoan() {
        Backendless.Data.of(Loan::class.java).save(loan, object : AsyncCallback<Loan> {
            override fun handleResponse(response: Loan?) {
                // Contact instance has been updated
                Log.d("SAVE LOAN","handle response: $response")
                Toast.makeText(this@LoanDetailActivity, "Loan successfully saved", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d("SAVE LOAN","HandleFault : $fault")
            }
        })
    }
}