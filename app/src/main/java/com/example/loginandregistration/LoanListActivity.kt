package com.example.loginandregistration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.loginandregistration.databinding.ActivityLoanListBinding

class LoanListActivity : AppCompatActivity() {
    companion object{
        val EXTRA_USERTHING = "ISER"
        val EXTRA_USER_ID = "7ssyer"
    }

    private lateinit var binding: ActivityLoanListBinding
    lateinit var adapter: LoanAdapter
    private lateinit var loans : List<Loan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loany = intent.getParcelableArrayListExtra<Loan>(EXTRA_USERTHING)
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        if(!loany.isNullOrEmpty())
        {
            loans = loany.toList()
            getLoans()
        }

        binding.fabLoanListCreateNewLoan.setOnClickListener {
            val loanDetailIntent = Intent(this, LoanDetailActivity::class.java).apply{
                putExtra(EXTRA_USER_ID,userId)
            }
            startActivity(loanDetailIntent)
        }
    }
    override fun onStart(){
        super.onStart()
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        if(userId != null){
            retrieveAllData(userId)
        }
    }
    override fun onResume(){
        super.onResume()
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        if(userId != null){
            retrieveAllData(userId)
        }
    }

    private fun retrieveAllData(userId: String){
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.setWhereClause( whereClause );
        Backendless.Data.of(Loan::class.java).find(queryBuilder,object :
            AsyncCallback<List<Loan?>?> {
            override fun handleResponse(foundLoan: List<Loan?>?) {
                // all Contact instances have been found
                Log.d(LoginActivity.TAG, "foundLoan: $foundLoan")
                var wong = ArrayList<Loan>()
                if (foundLoan != null) {
                    var loanList = foundLoan as List<Loan>
                    loanList.forEach { Loan -> wong.add(Loan) }
                    loans = wong.toList()
                    getLoans()
                }
            }


            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(LoginActivity.TAG,"handleFault: ${fault.message}")
            }
        })
    }

    private fun getLoans(){
        adapter = LoanAdapter(loans)
        binding.RecyclerViewLoanList.adapter = adapter
        binding.RecyclerViewLoanList.layoutManager = LinearLayoutManager(null)
    }
}