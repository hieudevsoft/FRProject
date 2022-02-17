package com.devapp.fr.data.entities

import android.os.Parcelable
import com.devapp.fr.data.models.authens.BaseAccount
import java.io.Serializable


class UserProfile(
    public override var email:String="",
    public override var password:String="",
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
    public override var id:String=""
): BaseAccount(id,email,password)

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
):Serializable{
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