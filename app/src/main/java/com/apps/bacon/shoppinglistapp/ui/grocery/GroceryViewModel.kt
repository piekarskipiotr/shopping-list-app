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
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    fun getShoppingList(shoppingListId: Int) = shoppingListRepository.getShoppingListById(shoppingListId, FirebaseAuth.getInstance().currentUser!!.uid)

    fun getGroceryForShoppingList(shoppingListId: Int) = groceryRepository.getGroceryForShoppingList(shoppingListId, FirebaseAuth.getInstance().currentUser!!.uid)

    private fun updateShoppingList(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.Default) {
        shoppingListRepository.update(shoppingList)
    }

    fun insertNewGrocery(groceryName: String, amount: Int, shoppingListId: Int) = viewModelScope.launch(Dispatchers.Default) {
        val grocery =
            Grocery(
                0,
                groceryName,
                amount,
                false,
                shoppingListId,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        groceryRepository.insert(grocery)
    }

    private fun updateGrocery(grocery: Grocery) = viewModelScope.launch(Dispatchers.Default) {
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

    fun deleteGroceryOnSwipe(grocery: Grocery, shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.Default) {
        groceryRepository.delete(grocery)
        updateShoppingListOnGrocerySwipe(shoppingList, true, grocery.isDone)
    }

    fun undoDeletedGrocery(grocery: Grocery, shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.Default) {
        groceryRepository.insert(grocery)
        updateShoppingListOnGrocerySwipe(shoppingList, false, !grocery.isDone)
    }
}