package com.devapp.fr.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

object Validation {

    const val REGEX_NAME = "[a-z \\u00E0-\\u00FC]{1,50}"
    val VALID_EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    val VALID_DOB: Pattern = Pattern.compile("^[0-3]{1}[0-9]{1}/[0-1]{1}[1-2]{1}/[1-9]{1}[0-9]{3}\$", Pattern.CASE_INSENSITIVE)

    fun validateEmailField(emailStr: String?): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }

    fun validateNameField(name:String):Boolean{
        return REGEX_NAME.toRegex().matches(name)
    }

    fun validatePasswordField(password:String):Boolean{
        return password.length>=8
    }

    fun validateConfirmPasswordFiled(password:String,confirmPassword:String):Boolean{
        return password==confirmPassword
    }

    fun validateSignUpFields(email:String,name:String,pass:String,confirmPass:String):Boolean{
        return validateEmailField(email) && validateNameField(name) && validatePasswordField(pass) && validateConfirmPasswordFiled(pass,confirmPass)
    }


    fun validateDobField(date:String):Boolean{
        val matcher: Matcher = VALID_DOB.matcher(date)
        return matcher.find()
    }
}