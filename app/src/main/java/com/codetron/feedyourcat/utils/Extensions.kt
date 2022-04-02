package com.codetron.feedyourcat.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatString():String{
    val sdf = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    return sdf.format(this)
}