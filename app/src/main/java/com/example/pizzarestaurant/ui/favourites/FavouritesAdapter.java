package com.example.pizzarestaurant.ui.favourites;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurant.R;
import com.example.pizzarestaurant.DatabaseHelper;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private List<ContentValues> favoriteList;
    private OnFavoriteClickListener listener;
    private String userEmail;

    public FavouritesAdapter(List<ContentValues> favoriteList, OnFavoriteClickListener listener, String userEmail) {
        this.favoriteList = favoriteList;
        this.listener = listener;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pizza, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        ContentValues pizza = favoriteList.get(position);
        String pizzaName = pizza.getAsString("pizza_name");
        holder.pizzaNameTextView.setText(pizzaName);

        holder.removeFromFavoritesButton.setImageResource(R.drawable.ic_filled_add_24);

        holder.removeFromFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveFromFavoritesClick(pizzaName);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPizzaLayoutClick(pizza);
            }
        });

        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOrderButtonClick(pizzaName, userEmail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void updateList(List<ContentValues> newList) {
        favoriteList = newList;
        notifyDataSetChanged();
    }

    static class FavouritesViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaNameTextView;
        ImageButton removeFromFavoritesButton;
        Button orderButton;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            removeFromFavoritesButton = itemView.findViewById(R.id.btn_add_to_favorites);
            orderButton = itemView.findViewById(R.id.btn_order);
        }
    }

    public interface OnFavoriteClickListener {
        void onRemoveFromFavoritesClick(String pizzaName);
        void onPizzaLayoutClick(ContentValues pizza);
        void onOrderButtonClick(String pizzaName, String userEmail);
    }
}
