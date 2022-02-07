package com.vk.dachecker.shopinglist.activities

import android.app.Application
import com.vk.dachecker.shopinglist.db.MainDataBase
import kotlinx.coroutines.InternalCoroutinesApi

class MainApp : Application() {
    @InternalCoroutinesApi
    val database by lazy { MainDataBase.getDataBase(this) }
}