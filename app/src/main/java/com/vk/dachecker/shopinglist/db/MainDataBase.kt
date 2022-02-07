package com.vk.dachecker.shopinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vk.dachecker.shopinglist.entities.LibraryItem
import com.vk.dachecker.shopinglist.entities.NoteItem
import com.vk.dachecker.shopinglist.entities.ShopListItem
import com.vk.dachecker.shopinglist.entities.ShopListNameItem
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [LibraryItem::class, NoteItem::class,
        ShopListNameItem::class, ShopListItem::class], version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null

        @InternalCoroutinesApi
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }

}