package com.apps.bacon.shoppinglistapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apps.bacon.shoppinglistapp.data.converters.DateConverter
import com.apps.bacon.shoppinglistapp.data.dao.GroceryDao
import com.apps.bacon.shoppinglistapp.data.dao.ShoppingListDao
import com.apps.bacon.shoppinglistapp.data.dao.UserDao
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(
    entities = [ShoppingList::class, Grocery::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun groceryDao(): GroceryDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME: String = "shopping_app_database"

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
//                            CoroutineScope(Dispatchers.Default).launch {
//                                instance!!.shoppingListDao().insert(ShoppingList(1, "Example list", 1, 2, Date(), false))
//                                instance!!.groceryDao().insert(Grocery(0, "Banana", 7, false, 1))
//                                instance!!.groceryDao().insert(Grocery(0, "Blueberry", 47, true, 1))
//                            }
                        }
                    })
                    .build()
            return instance!!
        }
    }
}