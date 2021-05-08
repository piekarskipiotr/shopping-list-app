package com.apps.bacon.shoppinglistapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.shoppinglistapp.adapters.GroceryAdapter
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.databinding.ActivityGroceryBinding
import com.apps.bacon.shoppinglistapp.viewmodel.GroceryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class GroceryActivity : AppCompatActivity(), GroceryAdapter.OnGroceryClickListener {
    private lateinit var binding: ActivityGroceryBinding
    private lateinit var groceryAdapter: GroceryAdapter
    private lateinit var shoppingList: ShoppingList
    private val groceryViewModel: GroceryViewModel by viewModels()
    private var isShoppingListIdArchived by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroceryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //get extras
        val shoppingListId = intent.extras!!.getInt(SHOPPING_LIST_ID_KEY)
        isShoppingListIdArchived = intent.extras!!.getBoolean(IS_SHOPPING_LIST_ARCHIVED_KEY)

        initRecyclerView(isShoppingListIdArchived)
        //get shoppingList object
        shoppingList = groceryViewModel.getShoppingList(shoppingListId)

        groceryViewModel.getGroceryForShoppingList(shoppingListId).observe(this, {
            groceryAdapter.updateData(it)

            //check if this is new empty list (all groceries = 0)
            isShoppingListIdArchived = if (shoppingList.allGroceries == 0)
                false
            else
                shoppingList.allGroceries == shoppingList.doneGroceries
        })

        if (isShoppingListIdArchived)
            disableAddingButton()
        else {
            binding.addGroceryButton.setOnClickListener {
                //TODO: insert new grocery
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView(isArchived: Boolean) {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            groceryAdapter = GroceryAdapter(this@GroceryActivity, this@GroceryActivity, isArchived)
            adapter = groceryAdapter
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(this@GroceryActivity, R.drawable.divider)!!)
            }
            addItemDecoration(itemDecoration)
        }
    }

    private fun disableAddingButton() {
        binding.addGroceryButton.isClickable = false
        binding.addGroceryButton.alpha = DISABLE_ALPHA
    }

    override fun OnGroceryClick(grocery: Grocery) {
        groceryViewModel.updateShoppingListDoneGroceriesValue(shoppingList, grocery.isDone)
        groceryViewModel.updateGroceryStatus(grocery)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (shoppingList.isArchived != isShoppingListIdArchived) {
            groceryViewModel.setShoppingListAsArchived(shoppingList)
        }
    }

    companion object {
        const val SHOPPING_LIST_ID_KEY = "SHOPPING_LIST_ID"
        const val IS_SHOPPING_LIST_ARCHIVED_KEY = "IS_SHOPPING_LIST_ARCHIVED"
        const val DISABLE_ALPHA = 0.7f
    }
}