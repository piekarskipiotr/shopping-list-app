package com.apps.bacon.shoppinglistapp.ui.grocery;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.apps.bacon.shoppinglistapp.R;
import com.apps.bacon.shoppinglistapp.data.entities.Grocery;
import com.apps.bacon.shoppinglistapp.databinding.GroceryItemBinding;
import org.jetbrains.annotations.NotNull;

public class GroceryAdapter extends ListAdapter<Grocery, GroceryAdapter.ViewHolder> {
    private final Context context;
    private final OnGroceryClickListener onGroceryClick;
    private final Boolean isArchived;

    public GroceryAdapter(Context context, OnGroceryClickListener onGroceryClick, Boolean isArchived) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onGroceryClick = onGroceryClick;
        this.isArchived = isArchived;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnGroceryClickListener onGroceryClickListener;
        TextView title, secondText;
        ImageView statusIcon;

        public ViewHolder(@NonNull GroceryItemBinding itemView, OnGroceryClickListener onGroceryClickListener) {
            super(itemView.getRoot());
            title = itemView.title;
            secondText = itemView.secondText;
            statusIcon = itemView.isDoneIcon;

            this.onGroceryClickListener = onGroceryClickListener;
            if(!isArchived)
                itemView.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGroceryClickListener.onGroceryClick(getItem(getAdapterPosition()));
            notifyItemChanged(getAdapterPosition());
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        GroceryItemBinding binding = GroceryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, onGroceryClick);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Grocery grocery = getItem(position);

        holder.title.setText(grocery.getName());
        String xSign = context.getString(R.string.x_sign);
        String groceryPiecesText = xSign + grocery.getPieces();
        holder.secondText.setText(groceryPiecesText);

        if(grocery.isDone()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.secondText.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_check_circle));
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.secondText.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_check_circle_outline));
        }
    }

    public interface OnGroceryClickListener {
         void onGroceryClick(Grocery grocery);
    }

    public static final DiffUtil.ItemCallback<Grocery> DIFF_CALLBACK = new DiffUtil.ItemCallback<Grocery>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Grocery oldItem, @NonNull @NotNull Grocery newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Grocery oldItem, Grocery newItem) {
            return oldItem.isDone() == newItem.isDone();
        }
    };
}
