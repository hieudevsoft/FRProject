package com.devapp.fr.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

object Validation {

    const val REGEX_NAME = "[a-z \\u00E0-\\u00FC]{1,50}"
    private val VALID_EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    private val VALID_DOB: Pattern = Pattern.compile("(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d\$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)\$)", Pattern.CASE_INSENSITIVE)

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

    fun validateDobField(date:String):Boolean{
        val matcher: Matcher = VALID_DOB.matcher(date)
        return matcher.find()
    }
}