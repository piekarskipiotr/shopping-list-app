package com.apps.bacon.shoppinglistapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.databinding.ActivityHomeBinding
import com.apps.bacon.shoppinglistapp.ui.grocery.GroceryActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), ShoppingListsAdapter.OnShoppingListClickListener,
    ShoppingListDialog.ShoppingListDialogListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var shoppingListAdapter: ShoppingListsAdapter
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    @Named("shopping_list_id_key")
    lateinit var shoppingListIdKey: String
    @Inject
    @Named("is_shopping_list_archived_key")
    lateinit var isShoppingListArchivedKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        initTabs()

        homeViewModel.shoppingListFilteredByArchived.observe(this, {
            shoppingListAdapter.submitList(it)
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    homeViewModel.setSelectedTab(tab.position)
                    binding.recyclerView.scheduleLayoutAnimation()
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
        val shoppingListDialog = ShoppingListDialog().newInstance()
        shoppingListDialog.show(supportFragmentManager, getString(R.string.shopping_list_dialog_tag))
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
            val tab = binding.tabLayout.newTab().setText(listOfTitles[i]).apply {
                icon = listOfIcons[i]
            }
            binding.tabLayout.addTab(tab, i)
        }

        //select tab on screen rotation
        homeViewModel.selectedTab.value?.let {
            binding.tabLayout.getTabAt(it)
        }?.select()
    }

    override fun onShoppingListClick(shoppingListId: Int, isArchived: Boolean) {
        intent = Intent(this, GroceryActivity::class.java)
        intent.putExtra(shoppingListIdKey, shoppingListId)
        intent.putExtra(isShoppingListArchivedKey, isArchived)
        startActivity(intent)
    }

    override fun onInsertButtonClick(shoppingListName: String) {
        homeViewModel.insertNewShoppingList(shoppingListName)
    }
}