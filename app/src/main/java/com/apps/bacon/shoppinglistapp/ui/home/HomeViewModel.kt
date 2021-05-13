package com.apps.bacon.shoppinglistapp.ui.home

import androidx.lifecycle.*
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
            false
        )
        shoppingListRepository.insert(shoppingList)
    }

    val shoppingListFilteredByArchived: LiveData<List<ShoppingList>> = Transformations.switchMap(selectedTab) {
        shoppingListRepository.getShoppingListsByArchivedStatus(it)
    }
}