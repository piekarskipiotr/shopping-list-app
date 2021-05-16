package com.apps.bacon.shoppinglistapp.utils;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class CustomDividerItemDecorator extends RecyclerView.ItemDecoration {
    private final Drawable divider;

    public CustomDividerItemDecorator(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void onDrawOver(@NotNull Canvas canvas, RecyclerView parent, @NotNull RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = Objects.requireNonNull(parent.getAdapter()).getItemCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(canvas);
        }
    }
}