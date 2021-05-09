package com.apps.bacon.shoppinglistapp.di

import android.content.Context
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
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
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideUserRepository(
        database: AppDatabase
    ) = ShoppingListRepository(database)

    @Provides
    @Singleton
    fun provideWeeklyStatsRepository(
        database: AppDatabase
    ) = GroceryRepository(database)
}