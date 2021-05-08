package com.apps.bacon.shoppinglistapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.databinding.ShoppingListItemBinding

class ShoppingListsAdapter constructor(
    private val listener: OnShoppingListClickListener,
    private val context: Context
) : RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>() {
    private var data: List<ShoppingList> = ArrayList()
    private var groceriesNumbers: List<List<Int>> = ArrayList()

    inner class ViewHolder(view: ShoppingListItemBinding) : RecyclerView.ViewHolder(view.root), View.OnClickListener {
        val title = view.title
        val secondText = view.secondText

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onShoppingListClick(data[adapterPosition].id, data[adapterPosition].isArchived)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ShoppingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingList = data[position]
        val shoppingListGroceriesNumbers = groceriesNumbers[position]

        //at index 0 all at 1 done
        val allGrocery = shoppingListGroceriesNumbers[0]
        val doneGrocery = shoppingListGroceriesNumbers[1]

        holder.title.text = shoppingList.name
        holder.secondText.text = context.resources.getString(R.string.shopping_list_item_second_text_prefix) + " $doneGrocery/$allGrocery"
    }

    override fun getItemCount() = data.size

    fun updateData(dataList: List<ShoppingList>) {
        data = dataList
        notifyDataSetChanged()
    }

    fun updateGroceriesNumbers(dataList: List<List<Int>>) {
        groceriesNumbers = dataList
    }

    interface OnShoppingListClickListener {
        fun onShoppingListClick(shoppingListId: Int, isArchived: Boolean)
    }
}