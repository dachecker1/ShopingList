package com.vk.dachecker.shopinglist.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm - dd.MM.yyyy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}