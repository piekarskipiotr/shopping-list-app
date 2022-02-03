package com.apps.bacon.shoppinglistapp.ui.home

import androidx.lifecycle.*
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import com.apps.bacon.shoppinglistapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    var selectedTab = MutableLiveData<Int>().apply {
        //init value on start
        value = 0
    }

    fun setSelectedTab(selectedTabI: Int) {
        selectedTab.value = selectedTabI
    }

    fun insertNewShoppingList(shoppingListName: String) = viewModelScope.launch(Dispatchers.IO) {
        val shoppingList = ShoppingList(
            Date().time,
            shoppingListName,
            0,
            0,
            false,
            auth.currentUser!!.uid,
        )
        shoppingListRepository.insert(shoppingList)
    }

    private var swipedShoppingListId: Long = -1L
    private var deleteGroceryJob: Job? = null

    private fun deleteGroceryFromDeletedShoppingList() = viewModelScope.launch(Dispatchers.IO) {
        delay(3000)
        groceryRepository.deleteGroceryFromDeletedShoppingList(swipedShoppingListId, auth.currentUser!!.uid)
    }

    fun undoDeletedShoppingList(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.IO) {
        shoppingListRepository.insert(shoppingList)
        deleteGroceryJob!!.cancel()
    }

    fun deleteShoppingListOnSwipe(shoppingList: ShoppingList) = viewModelScope.launch(Dispatchers.IO) {
        shoppingListRepository.delete(shoppingList)

        swipedShoppingListId = shoppingList.id
        deleteGroceryJob = deleteGroceryFromDeletedShoppingList()
        deleteGroceryJob!!.start()
    }

    val shoppingListFilteredByArchived = Transformations.switchMap(selectedTab) {
        shoppingListRepository.getShoppingListsByArchivedStatus(it, auth.currentUser!!.uid)
    }
}