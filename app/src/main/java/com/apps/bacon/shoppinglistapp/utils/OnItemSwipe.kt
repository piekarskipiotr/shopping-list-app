package com.apps.bacon.shoppinglistapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.shoppinglistapp.R
import com.apps.bacon.shoppinglistapp.ui.grocery.GroceryAdapter
import com.apps.bacon.shoppinglistapp.ui.home.ShoppingListsAdapter
import com.google.android.material.snackbar.Snackbar


class OnItemSwipe constructor(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val listener: OnSwipe,
    private val icon: Drawable
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val adapter = try {
            recyclerView.adapter as ShoppingListsAdapter
        } catch (e: Exception) {
            recyclerView.adapter as GroceryAdapter
        }

        val item = adapter.currentList[position]
        listener.deleteOnItemSwipe(item)

        Snackbar.make(context, recyclerView, "Item has been deleted!", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                listener.undoDeletedItem(item)
            }.show()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        val background = ColorDrawable()
        val backgroundColor = ContextCompat.getColor(context, R.color.red)
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of delete icon
        val intrinsicWidth = icon.intrinsicWidth
        val intrinsicHeight = icon.intrinsicHeight

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        icon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    interface OnSwipe {
        fun deleteOnItemSwipe(item: Any)
        fun undoDeletedItem(item: Any)
    }
}