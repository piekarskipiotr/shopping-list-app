package com.apps.bacon.shoppinglistapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "grocery")
data class Grocery(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "pieces")
    var pieces: Int,

    @ColumnInfo(name = "is_done")
    var isDone: Boolean,

    @ColumnInfo(name = "shopping_list_id")
    var shoppingListId: Int,

    @ColumnInfo(name = "user_id")
    var userId: String
) {
    constructor() : this(0, "", 0, false, 0,"")
}