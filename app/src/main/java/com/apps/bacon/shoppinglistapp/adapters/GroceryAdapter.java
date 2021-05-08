package com.apps.bacon.shoppinglistapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.apps.bacon.shoppinglistapp.R;
import com.apps.bacon.shoppinglistapp.data.entities.Grocery;
import com.apps.bacon.shoppinglistapp.databinding.GroceryItemBinding;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {
    private final Context mContext;
    private final OnGroceryClickListener mOnGroceryClick;
    private final Boolean isShoppingListArchived;
    private List<Grocery> data;
    private int size = 0;

    public GroceryAdapter(Context context, OnGroceryClickListener onGroceryClick, Boolean isArchived){
        mContext = context;
        mOnGroceryClick = onGroceryClick;
        isShoppingListArchived = isArchived;
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
            if(!isShoppingListArchived)
                itemView.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGroceryClickListener.OnGroceryClick(data.get(getAdapterPosition()));
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        GroceryItemBinding binding = GroceryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, mOnGroceryClick);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Grocery grocery = data.get(position);

        holder.title.setText(grocery.getName());
        holder.secondText.setText("X"+grocery.getPieces());
        if(grocery.isDone()){
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.secondText.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_round_check_circle));
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.secondText.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_round_check_circle_outline));
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void updateData(List<Grocery> dataList) {
        data = dataList;
        size = data.size();
        notifyDataSetChanged();
    }

    public interface OnGroceryClickListener {
         void OnGroceryClick(Grocery grocery);
    }
}
