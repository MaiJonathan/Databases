package com.example.loginandregistration

import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import android.widget.Toast.makeText
import com.example.loginandregistration.RegistrationUtil.existingEmails

// object keyword makes it so all the functions are
// static functions
object RegistrationUtil {
    // use this in the test class for the is username taken test
    // make another similar list for some taken emails
    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")
    var existingEmails = listOf("LinusWong@gmail.com")
//    you can use listOf<type>() instead of making the list & adding individually
//    List<String> blah = new ArrayList<String>();
//    blah.add("hi")
//    blah.add("hello")
//

    // isn't empty
    // already taken
    // minimum number of characters is 3
    fun validateUsername(username: String) : Boolean {
        if(existingUsers.contains(username) || username.length <= 3){
            return false
        }
        return true
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        if(password.isEmpty()) return false

        var hasDigit = false
        var hasCapital = false
        var IsSamePass = false
        var minLength = false

        if(password.equals(confirmPassword)){
            IsSamePass = true
        }
        if(password.length >= 8){
            minLength = true
        }
        for(char in password)
        {
            if(char.isDigit()) hasDigit = true
            if(char.isUpperCase()) hasCapital = true
        }
        return IsSamePass && minLength && hasCapital && hasDigit
    }

    // isn't empty
    fun validateName(name: String) : Boolean {
        if(name.isEmpty()){
            return false
        }
        return true
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email:String):Boolean{
        if(!email.contains("@") || !email.contains(".") || (email.length <= 5) || existingEmails.contains(email)) return false
        var hasName = false
        var hasServer = false
        var hasDomain = false
        //Checks Name
        val name = email.substring(0,email.indexOf("@"))
        if((name.isNotEmpty())) hasName = true
        for (char in name)
        {
            if(!char.isLetterOrDigit()) hasName = false
        }
        //Checks server
        val server = email.substring(email.indexOf("@")+1,email.indexOf(".") )
        if((server.isNotEmpty())) hasServer = true
        for (char in server)
        {
            if(!char.isLetterOrDigit()) hasServer = false
        }
        //Checks domain
        val domain = email.substring(email.lastIndexOf(".")+1)
        if((domain.isNotEmpty())) hasDomain = true
        for (char in domain)
        {
            if(!char.isLetterOrDigit()) hasDomain = false
        }

        return hasName && hasServer && hasDomain

    }
}
