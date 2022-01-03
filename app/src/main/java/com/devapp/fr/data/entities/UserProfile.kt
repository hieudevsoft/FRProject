package com.devapp.fr.data.entities

import com.devapp.fr.data.models.authens.BaseAccount

class UserProfile(
    override var email:String="",
    override var password:String="",
    var name:String="",
    var gender:Int=0,
    var dob:String="",
    var address:String="",
    //var images:List<String>?= mutableListOf(),
    var interests:List<String>?= mutableListOf(),
    override var id:String=""
): BaseAccount(id,email,password) {
}