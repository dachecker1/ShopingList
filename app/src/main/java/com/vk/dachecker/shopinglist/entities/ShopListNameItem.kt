package com.vk.dachecker.shopinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "shopping_list_names")
data class ShopListNameItem(
    //первый столбец
    @PrimaryKey (autoGenerate = true) //каждому элементу присваивает уникальный идентификатор
    val id : Int?, // val id хранит в себе primaryKey  и генерируется автоматически
    //второй столбец, в название записываем название списка, типа "Воскресная покупка".
    @ColumnInfo (name = "name")
    val name: String,

    @ColumnInfo (name = "Time")
    val time: String,

    @ColumnInfo (name = "allItemCount")
    val allItemCounter: Int,

    @ColumnInfo (name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo (name = "itemsIds")
    val itemsIds: String,

) :Serializable
