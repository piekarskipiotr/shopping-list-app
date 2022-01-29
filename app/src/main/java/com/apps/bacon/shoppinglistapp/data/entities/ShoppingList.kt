package com.apps.bacon.shoppinglistapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "done_groceries")
    var doneGroceries: Int,

    @ColumnInfo(name = "all_groceries")
    var allGroceries: Int,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "is_archived")
    var isArchived: Boolean,

    @ColumnInfo(name = "user_id")
    var userId: String
)