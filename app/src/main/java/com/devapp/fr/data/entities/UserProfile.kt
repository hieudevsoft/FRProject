package com.devapp.fr.data.entities

import com.devapp.fr.data.models.authens.BaseAccount

class UserProfile(
    override var id:String="",
    override var email:String="",
    override var password:String="",
    var name:String="",
    var dob:String="",
    var address:String="",
    var images:List<String>?= mutableListOf(),
    var interests:List<String>?= mutableListOf()
    ): BaseAccount(id,email,password) {
}