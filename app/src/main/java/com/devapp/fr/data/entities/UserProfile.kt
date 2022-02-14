package com.devapp.fr.data.entities

import com.devapp.fr.data.models.authens.BaseAccount

class UserProfile(
    override var email:String="",
    override var password:String="",
    var name:String="",
    var bio:String="",
    var coins:Int=0,
    var likes:Int=0,
    var gender:Int=0,
    var purpose:Int=-1,
    var dob:String="",
    var address:String="",
    var job:String="",
    var interests:List<Int>?= mutableListOf(),
    var additionInformation:AdditionInformation?=null,
    var images:List<String>?= mutableListOf(),
    override var id:String=""
): BaseAccount(id,email,password) {
}

data class AdditionInformation(
    var tall:Int=-1,
    var child:Int=-1,
    var drink:Int=-1,
    var maritalStatus:Int=-1,
    var trueGender:Int=-1,
    var smoking:Int=-1,
    var pet:Int=-1,
    var religion:Int=-1,
    var certificate:Int=-1,
    var personality:Int=-1,
){
    fun setNewDataByName(name:String,data:Int){
        when(name){
            "tall"->tall=data
            "child"->child=data
            "drink"->drink=data
            "maritalStatus"->maritalStatus=data
            "trueGender"->trueGender=data
            "smoking"->smoking=data
            "pet"->pet=data
            "religion"->religion=data
            "certificate"->certificate=data
            "personality"->personality=data
        }
    }
}