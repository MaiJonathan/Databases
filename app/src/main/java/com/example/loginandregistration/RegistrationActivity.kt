package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityLoginBinding
import com.example.loginandregistration.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve any information from the intent
        var username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        var password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        //prefill the username and password fields
        //for EditTexts, you actually have to use the setText functions
        binding.editTextTextPersonNameRegistrationUsername.setText(username)
        binding.editTextTextPasswordRegistrationPassword.setText(password)


        binding.buttonRegistrationRegister.setOnClickListener {
            val confirm = binding.editTextTextPasswordRegistrationConfirmPassword.text.toString()
            val name = binding.editTextTextPersonNameRegistrationName.text.toString()
            val email = binding.editTextTextPersonNameRegistrationEmail.text.toString()
            password = binding.editTextTextPasswordRegistrationPassword.text.toString()
            username = binding.editTextTextPersonNameRegistrationUsername.text.toString()
            if(RegistrationUtil.validatePassword(password,confirm)
                && RegistrationUtil.validateUsername(username)
                && RegistrationUtil.validateName(name)
                && RegistrationUtil.validateEmail(email)) {
                //apply lambda will call the functions inside it on the object
                //that apply is called on

                val user = BackendlessUser()
                user.setProperty("email", email)
                user.setProperty("username", username)
                user.setProperty("name",name)
                user.password = password
                Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(registeredUser: BackendlessUser?) {
                        // user has been registered and now can login
                        Log.d(LoginActivity.TAG, "handleResponse: ${user?.getProperty("username")}: has registered in")
                        var resultIntent = Intent().apply {
                            //apply { putExtra() } is doing the same thing as resultIntent.putExtra()
                            putExtra(
                                LoginActivity.EXTRA_USERNAME,
                                binding.editTextTextPersonNameRegistrationUsername.text.toString()
                            )
                            putExtra(
                                LoginActivity.EXTRA_PASSWORD,
                                binding.editTextTextPasswordRegistrationPassword.text.toString()
                            )
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Log.d(LoginActivity.TAG,"handleFault: ${fault.message}")

                    }
                })


            }
        }
    }
}