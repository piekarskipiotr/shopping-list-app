package com.apps.bacon.shoppinglistapp.di

import android.content.Context
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import com.apps.bacon.shoppinglistapp.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @Named("name_error")
    fun getNameErrorMessage(@ApplicationContext context: Context): String = context.getString(R.string.name_error_message)

    @Provides
    @Singleton
    @Named("amount_error")
    fun getAmountErrorMessage(@ApplicationContext context: Context): String = context.getString(R.string.amount_error_message)

    @Provides
    @Singleton
    @Named("shopping_list_id_key")
    fun getShoppingListIdKey(@ApplicationContext context: Context): String = context.getString(R.string.shopping_list_id_key)

    @Provides
    @Singleton
    @Named("is_shopping_list_archived_key")
    fun getShoppingListArchivedKey(@ApplicationContext context: Context): String = context.getString(R.string.is_shopping_list_archived_key)

    @Provides
    @Singleton
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideShoppingListRepository(
        database: AppDatabase
    ) = ShoppingListRepository(database)

    @Provides
    @Singleton
    fun provideGroceryRepository(
        database: AppDatabase
    ) = GroceryRepository(database)

    @Provides
    @Singleton
    fun provideUserRepository(
        database: AppDatabase
    ) = UserRepository(database)
}