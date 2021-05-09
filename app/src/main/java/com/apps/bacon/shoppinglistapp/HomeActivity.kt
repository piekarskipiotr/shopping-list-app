package com.apps.bacon.shoppinglistapp

import android.content.Intent
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
class HomeActivity : AppCompatActivity(), ShoppingListsAdapter.OnShoppingListClickListener, ShoppingListDialog.ShoppingListDialogListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var shoppingListAdapter: ShoppingListsAdapter
    val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        initTabs()

        homeViewModel.shoppingListFilteredByArchived.observe(this, {
            shoppingListAdapter.updateData(it)
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    homeViewModel.setSelectedTab(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.addShoppingListButton.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val shoppingListDialog: ShoppingListDialog = ShoppingListDialog().newInstance()
        shoppingListDialog.show(supportFragmentManager, SHOPPING_LIST_DIALOG_TAG)
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

        //add tabs
        val size = listOfTitles.size - 1
        for (i in 0..size) {
            val isSelected = i == 0
            val tab = binding.tabLayout.newTab().setText(listOfTitles[i]).apply {
                icon = listOfIcons[i]
            }
            binding.tabLayout.addTab(tab, i, isSelected)
        }

        //select tab on screen rotation
        homeViewModel.selectedTab.value?.let {
            binding.tabLayout.getTabAt(it)
        }?.select()
    }

    override fun onShoppingListClick(shoppingListId: Int, isArchived: Boolean) {
        intent = Intent(this, GroceryActivity::class.java)
        intent.putExtra(SHOPPING_LIST_ID_KEY, shoppingListId)
        intent.putExtra(IS_SHOPPING_LIST_ARCHIVED_KEY, isArchived)
        startActivity(intent)
    }

    override fun onInsertButtonClick(shoppingListName: String) {
        homeViewModel.insertNewShoppingList(shoppingListName)
    }

    companion object {
        const val SHOPPING_LIST_ID_KEY = "SHOPPING_LIST_ID"
        const val IS_SHOPPING_LIST_ARCHIVED_KEY = "IS_SHOPPING_LIST_ARCHIVED"
        const val SHOPPING_LIST_DIALOG_TAG = "insert new shopping list dialog"
    }
}