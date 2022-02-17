package com.devapp.fr.data.models.items

import java.text.SimpleDateFormat
import java.util.*

data class Notification(var message:String,var time:String = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date(Calendar.getInstance().time.time)))
{
    constructor():this("")
}