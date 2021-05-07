package com.apps.bacon.shoppinglistapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.shoppinglistapp.adapters.ShoppingListsAdapter
import com.apps.bacon.shoppinglistapp.databinding.ActivityHomeBinding
import com.apps.bacon.shoppinglistapp.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), ShoppingListsAdapter.OnShoppingListClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var shoppingListAdapter: ShoppingListsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val homeViewModel: HomeViewModel by viewModels()
        initRecyclerView()


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.position){
                        0 -> {
                            homeViewModel.getArchivedShoppingLists().removeObservers(this@HomeActivity)
                            homeViewModel.getActiveShoppingLists().observe(this@HomeActivity, {
                                val activeShoppingListsGroceryNumbers: MutableList<MutableList<Int>> = mutableListOf()
                                for (shoppingList in it){
                                    val list = mutableListOf(
                                        homeViewModel.getNumberOfAllGroceryInShoppingList(shoppingList.id),
                                        homeViewModel.getNumberOfDoneGroceryInShoppingList(shoppingList.id)
                                    )
                                    activeShoppingListsGroceryNumbers.add(list)
                                }
                                shoppingListAdapter.updateData(it)
                                shoppingListAdapter.updateGroceriesNumbers(activeShoppingListsGroceryNumbers)
                            })
                        }

                        1 -> {
                            homeViewModel.getActiveShoppingLists().removeObservers(this@HomeActivity)
                            homeViewModel.getArchivedShoppingLists().observe(this@HomeActivity, {
                                val archivedShoppingListsGroceryNumbers: MutableList<MutableList<Int>> = mutableListOf()
                                for (shoppingList in it){
                                    val list = mutableListOf(
                                        homeViewModel.getNumberOfAllGroceryInShoppingList(shoppingList.id),
                                        homeViewModel.getNumberOfDoneGroceryInShoppingList(shoppingList.id)
                                    )
                                    archivedShoppingListsGroceryNumbers.add(list)
                                }
                                shoppingListAdapter.updateData(it)
                                shoppingListAdapter.updateGroceriesNumbers(archivedShoppingListsGroceryNumbers)
                            })
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.addShoppingListButton.setOnClickListener {
            //TODO: open the form for adding a new list
        }

        initTabs()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            shoppingListAdapter = ShoppingListsAdapter(this@HomeActivity, this@HomeActivity)
            adapter = shoppingListAdapter
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.divider)!!)
            }
            addItemDecoration(itemDecoration)
        }
    }

    private fun initTabs() {
        val listOfTitles =
            listOf(getString(R.string.shopping_lists), getString(R.string.archived_shopping_lists))
        val listOfIcons = listOf(
            ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_round_list),
            ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_round_archive)
        )

        val size = listOfTitles.size - 1
        for (i in 0..size) {
            val isSelected = i == 0
            val tab = binding.tabLayout.newTab().setText(listOfTitles[i]).apply {
                icon = listOfIcons[i]
            }
            binding.tabLayout.addTab(tab, i, isSelected)
        }
    }

    override fun onShoppingListClick(shoppingListId: Int) {
        //TODO: start activity
    }
}