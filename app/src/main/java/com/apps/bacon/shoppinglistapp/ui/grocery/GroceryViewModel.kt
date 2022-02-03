package com.apps.bacon.shoppinglistapp.ui.grocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    fun getShoppingList(shoppingListId: Long) = shoppingListRepository.getShoppingListById(shoppingListId, FirebaseAuth.getInstance().currentUser!!.uid)

    fun getGroceryForShoppingList(shoppingListId: Long) = groceryRepository.getGroceryForShoppingList(shoppingListId, FirebaseAuth.getInstance().currentUser!!.uid)

    private fun updateShoppingList(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.IO) {
        shoppingListRepository.update(shoppingList)
    }

    fun insertNewGrocery(groceryName: String, amount: Int, shoppingListId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val grocery =
            Grocery(
                Date().time,
                groceryName,
                amount,
                false,
                shoppingListId,
                FirebaseAuth.getInstance().currentUser!!.uid,
            )
        groceryRepository.insert(grocery)
    }

    private fun updateGrocery(grocery: Grocery) = viewModelScope.launch(Dispatchers.IO) {
        groceryRepository.update(grocery)
    }

    fun updateShoppingListAllGroceriesValue(shoppingList: ShoppingList) {
        shoppingList.apply { allGroceries += 1 }
        updateShoppingList(shoppingList)
    }

    fun updateShoppingListDoneGroceriesValue(shoppingList: ShoppingList, isDone: Boolean) {
        shoppingList.apply {
            doneGroceries += if (isDone) -1 else 1
        }
        updateShoppingList(shoppingList)
    }

    private fun updateShoppingListOnGrocerySwipe(shoppingList: ShoppingList, isDeleted: Boolean, isDone: Boolean) {
        shoppingList.apply {
            allGroceries += if (isDeleted) -1 else 1
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

    fun deleteGroceryOnSwipe(grocery: Grocery, shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.IO) {
        groceryRepository.delete(grocery)
        updateShoppingListOnGrocerySwipe(shoppingList, true, grocery.isDone)
    }

    fun undoDeletedGrocery(grocery: Grocery, shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.IO) {
        groceryRepository.insert(grocery)
        updateShoppingListOnGrocerySwipe(shoppingList, false, !grocery.isDone)
    }
}