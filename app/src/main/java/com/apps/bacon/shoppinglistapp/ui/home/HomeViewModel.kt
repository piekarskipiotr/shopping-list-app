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
    private val userRepository: UserRepository
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
            0,
            shoppingListName,
            0,
            0,
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

    fun sendData() = viewModelScope.launch(Dispatchers.IO) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // get local data
        val shoppingList = shoppingListRepository.getAllShoppingListForUser(userId)
        val groceries = groceryRepository.getAllGroceryForUser(userId)

        // create maps
        val shoppingListMap: MutableMap<String, ShoppingList> = mutableMapOf()
        val groceriesMap: MutableMap<String, Grocery> = mutableMapOf()

        for (e in shoppingList) {
            shoppingListMap[e.id.toString()] = e
        }

        for (e in groceries) {
            groceriesMap[e.id.toString()] = e
        }

        // send local data
        FirebaseFirestore.getInstance().collection("shoppinglists").document(userId).set(shoppingListMap).await()
        FirebaseFirestore.getInstance().collection("grocery").document(userId).set(groceriesMap).await()

        // update user changes date
        FirebaseFirestore.getInstance().collection("users").document(userId).update("lastUpdate", Date()).await()
        userRepository.updateSyncDate(userId)
    }

    val shoppingListFilteredByArchived = Transformations.switchMap(selectedTab) {
        shoppingListRepository.getShoppingListsByArchivedStatus(it, FirebaseAuth.getInstance().currentUser!!.uid)
    }
}