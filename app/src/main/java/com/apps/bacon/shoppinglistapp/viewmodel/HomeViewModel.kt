package com.apps.bacon.shoppinglistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val shoppingListFilteredByArchived: LiveData<List<ShoppingList>> = Transformations.switchMap(selectedTab) {
        shoppingListRepository.getShoppingListsByArchivedStatus(it)
    }
}