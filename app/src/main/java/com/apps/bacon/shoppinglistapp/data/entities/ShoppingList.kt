package com.apps.bacon.shoppinglistapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "done_groceries")
    var doneGroceries: Int,

    @ColumnInfo(name = "all_groceries")
    var allGroceries: Int,

    @ColumnInfo(name = "is_archived")
    var isArchived: Boolean,

    @ColumnInfo(name = "user_id")
    var userId: String,
) {
    constructor() : this(Date().time, "", 0, 0, false, "")
}