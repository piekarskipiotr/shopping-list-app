package com.apps.bacon.shoppinglistapp.ui.home

import androidx.lifecycle.*
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    var selectedTab = MutableLiveData<Int>().apply {
        //init value on start
        value = 0
    }

    fun setSelectedTab(selectedTabI: Int) {
        selectedTab.value = selectedTabI
    }

    fun insertNewShoppingList(shoppingListName: String) = viewModelScope.launch(Dispatchers.Default) {
        val shoppingList = ShoppingList(
            0,
            shoppingListName,
            0,
            0,
            Date(),
            false,
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        shoppingListRepository.insert(shoppingList)
    }

    private var swipedShoppingListId: Int = -1
    private var deleteGroceryJob: Job? = null

    private fun deleteGroceryFromDeletedShoppingList() = viewModelScope.launch(Dispatchers.Default) {
        delay(3000)
        groceryRepository.deleteGroceryFromDeletedShoppingList(swipedShoppingListId, FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun undoDeletedShoppingList(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.Default) {
        shoppingListRepository.insert(shoppingList)
        deleteGroceryJob!!.cancel()
    }

    fun deleteShoppingListOnSwipe(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.Default) {
        shoppingListRepository.delete(shoppingList)

        swipedShoppingListId = shoppingList.id
        deleteGroceryJob = deleteGroceryFromDeletedShoppingList()
        deleteGroceryJob!!.start()
    }

    val shoppingListFilteredByArchived = Transformations.switchMap(selectedTab) {
        shoppingListRepository.getShoppingListsByArchivedStatus(it, FirebaseAuth.getInstance().currentUser!!.uid)
    }
}