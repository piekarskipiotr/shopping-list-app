package com.apps.bacon.shoppinglistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    fun getShoppingList(shoppingListId: Int) = shoppingListRepository.getShoppingListById(shoppingListId)

    fun getGroceryForShoppingList(shoppingListId: Int) = groceryRepository.getGroceryForShoppingList(shoppingListId)

    private fun updateShoppingList(shoppingList: ShoppingList) = CoroutineScope(Dispatchers.Default).launch {
        shoppingListRepository.update(shoppingList)
    }

    fun insert(grocery: Grocery) = CoroutineScope(Dispatchers.Default).launch {
        groceryRepository.insert(grocery)
    }

    private fun updateGrocery(grocery: Grocery) = CoroutineScope(Dispatchers.Default).launch {
        groceryRepository.update(grocery)
    }

    fun updateShoppingListDoneGroceriesValue(shoppingList: ShoppingList, isDone: Boolean) {
        shoppingList.apply {
            doneGroceries += if (isDone) -1 else 1
        }
        updateShoppingList(shoppingList)
    }

    fun updateGroceryStatus(grocery: Grocery) {
        grocery.apply {
            isDone = !grocery.isDone
        }
        updateGrocery(grocery)
    }

    fun setShoppingListAsArchived(shoppingList: ShoppingList) {
        shoppingList.apply {
            isArchived = true
        }
        updateShoppingList(shoppingList)
    }
}