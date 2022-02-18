package com.devapp.fr.data.models.authens

import java.io.Serializable

abstract class
BaseAccount(
    protected open var id:String
    , protected open var email:String
    , protected open var password:String):Serializable