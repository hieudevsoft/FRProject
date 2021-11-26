package com.devapp.fr.util

object Validation {

    const val regexEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}\$"
    const val regexName = "[a-z \\u00E0-\\u00FC]{1,50}"

    fun validateEmailField(email:String):Boolean{
        return  regexEmail.toRegex().matches(email)
    }

    fun validateNameField(name:String):Boolean{
        return regexName.toRegex().matches(name)
    }

    fun validatePasswordField(password:String):Boolean{
        return password.length>=8
    }

    fun validateConfirmPasswordFiled(password:String,confirmPassword:String):Boolean{
        return password===confirmPassword
    }

    fun validateSignUpFields(email:String,name:String,pass:String,confirmPass:String):Boolean{
        return validateEmailField(email) && validateNameField(name) && validatePasswordField(pass) && validateConfirmPasswordFiled(pass,confirmPass)
    }

}