package com.apps.bacon.shoppinglistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val groceryRepository: GroceryRepository
) : ViewModel() {
    fun getActiveShoppingLists() = shoppingListRepository.getActiveShoppingLists()

    fun getArchivedShoppingLists() = shoppingListRepository.getArchivedShoppingLists()

    fun getNumberOfDoneGroceryInShoppingList(shoppingListId: Int) = groceryRepository.getNumberOfDoneGroceryInShoppingList(shoppingListId)

    fun getNumberOfAllGroceryInShoppingList(shoppingListId: Int) = groceryRepository.getNumberOfAllGroceryInShoppingList(shoppingListId)

    fun updateShoppingList(shoppingList: ShoppingList) = CoroutineScope(Dispatchers.Default).launch {
        shoppingListRepository.update(shoppingList)
    }

    fun deleteShoppingList(shoppingList: ShoppingList) = CoroutineScope(Dispatchers.Default).launch {
        shoppingListRepository.delete(shoppingList)
    }

    fun insertShoppingList(shoppingList: ShoppingList) = CoroutineScope(Dispatchers.Default).launch {
        shoppingListRepository.insert(shoppingList)
    }
}