package com.example.loginandregistration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.loginandregistration.LoanListActivity.Companion.EXTRA_USERTHING
import com.example.loginandregistration.LoanListActivity.Companion.EXTRA_USER_ID
import com.example.loginandregistration.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object{
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val TAG = "LoginActivity"
    }
    var loanList: List<Loan> = emptyList()
    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent to do whatever we need with the returned info
            binding.editTextTextUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextTextPassword2.setText(intent?.getStringExtra(EXTRA_PASSWORD))


        }
    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Backendless.initApp(this, constants.APP_ID,constants.API_KEY)

        binding.textViewLoginSignup.setOnClickListener {
            // 1. Create an intent object with the current activity
            //and the destination activity's class
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            //2 optionally add information to send with the intent
            //key-value pai rs just like with Bundles
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextTextUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextTextPassword2.text.toString())
//            //3a. launch the new activity using the intent
//            startActivity(registrationIntent)
            //3b. Launch the activity for a result using the variable from the register for result contract above
            startRegistrationForResult.launch(registrationIntent)
        }
        binding.buttonLoginLogin.setOnClickListener{
// do not forget to call Backendless.initApp in the app initialization code
            // do not forget to call Backendless.initApp in the app initialization code
            Backendless.UserService.login(
                binding.editTextTextUsername.text.toString(),
                binding.editTextTextPassword2.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in
                        Log.d(LoginActivity.TAG, "handleResponse: ${user?.getProperty("username")}: has logged in")
                        val userId = user!!.objectId
                        retrieveAllData(userId,it.context)
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d(LoginActivity.TAG,"handleFault: ${fault.message}")
                    }
                }
            )

        }
    }
    private fun retrieveAllData(userId: String, linus: Context){
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.setWhereClause( whereClause );
        Backendless.Data.of(Loan::class.java).find(queryBuilder,object : AsyncCallback<List<Loan?>?> {
            override fun handleResponse(foundLoan: List<Loan?>?) {
                // all Contact instances have been found
                Log.d(LoginActivity.TAG,"foundLoan: $foundLoan")
                var wong = ArrayList<Loan>()
                if (foundLoan != null) {
                    loanList = foundLoan as List<Loan>
                    loanList.forEach { Loan -> wong.add(Loan) }
                }
                val detailIntent = Intent(linus, LoanListActivity::class.java)
                detailIntent.putExtra(EXTRA_USERTHING,wong)
                detailIntent.putExtra(EXTRA_USER_ID,userId)
                linus.startActivity(detailIntent)
            }


            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(LoginActivity.TAG,"handleFault: ${fault.message}")
            }
        })
    }
}

