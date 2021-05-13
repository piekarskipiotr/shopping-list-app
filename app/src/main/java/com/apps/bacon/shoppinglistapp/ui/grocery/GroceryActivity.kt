package com.apps.bacon.shoppinglistapp.ui.grocery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.databinding.ActivityGroceryBinding
import com.apps.bacon.shoppinglistapp.utils.Button
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.Delegates

@AndroidEntryPoint
class GroceryActivity : AppCompatActivity(), GroceryAdapter.OnGroceryClickListener,
    GroceryDialog.GroceryDialogListener {
    private lateinit var binding: ActivityGroceryBinding
    private lateinit var groceryAdapter: GroceryAdapter
    private lateinit var shoppingList: ShoppingList
    private val groceryViewModel: GroceryViewModel by viewModels()
    private var shoppingListId by Delegates.notNull<Int>()
    private var isShoppingListIdArchived by Delegates.notNull<Boolean>()

    @Inject
    @Named("shopping_list_id_key")
    lateinit var shoppingListIdKey: String
    @Inject
    @Named("is_shopping_list_archived_key")
    lateinit var isShoppingListArchivedKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroceryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //get extras
        shoppingListId = intent.extras!!.getInt(shoppingListIdKey)
        isShoppingListIdArchived = intent.extras!!.getBoolean(isShoppingListArchivedKey)

        initRecyclerView(isShoppingListIdArchived)
        //get shoppingList object
        shoppingList = groceryViewModel.getShoppingList(shoppingListId)

        groceryViewModel.getGroceryForShoppingList(shoppingListId).observe(this) {
            groceryAdapter.updateData(it)

            //check if this is new empty list (all groceries = 0)
            isShoppingListIdArchived = if (shoppingList.allGroceries == 0)
                false
            else
                shoppingList.allGroceries == shoppingList.doneGroceries
        }

        if (isShoppingListIdArchived)
            disableAddingButton()
        else {
            binding.addGroceryButton.setOnClickListener {
                openDialog()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openDialog() {
        val groceryDialog: GroceryDialog = GroceryDialog().newInstance()
        groceryDialog.show(supportFragmentManager, getString(R.string.grocery_dialog_tag))
    }

    private fun initRecyclerView(isArchived: Boolean) {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            groceryAdapter =
                GroceryAdapter(
                    this@GroceryActivity,
                    this@GroceryActivity,
                    isArchived
                )
            adapter = groceryAdapter
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(this@GroceryActivity, R.drawable.divider)!!)
            }
            addItemDecoration(itemDecoration)
        }
    }

    private fun disableAddingButton() {
        binding.addGroceryButton.isClickable = false
        binding.addGroceryButton.alpha = Button.State.Disable.alpha
    }

    override fun OnGroceryClick(grocery: Grocery) {
        groceryViewModel.updateShoppingListDoneGroceriesValue(shoppingList, grocery.isDone)
        groceryViewModel.updateGroceryStatus(grocery)
    }

    override fun onInsertButtonClick(groceryName: String, amount: Int) {
        groceryViewModel.updateShoppingListAllGroceriesValue(shoppingList)
        groceryViewModel.insertNewGrocery(groceryName, amount, shoppingListId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (shoppingList.isArchived != isShoppingListIdArchived) {
            groceryViewModel.setShoppingListAsArchived(shoppingList)
        }
    }
}