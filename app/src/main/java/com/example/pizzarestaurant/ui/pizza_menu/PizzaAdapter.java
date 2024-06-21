package com.example.pizzarestaurant.ui.pizza_menu;

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

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<ContentValues> pizzaList;
    private OnPizzaClickListener listener;
    private String userEmail;

    public PizzaAdapter(List<ContentValues> pizzaList, OnPizzaClickListener listener, String userEmail) {
        this.pizzaList = pizzaList;
        this.listener = listener;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        ContentValues pizza = pizzaList.get(position);
        String pizzaName = pizza.getAsString("pizza_name");
        holder.pizzaNameTextView.setText(pizzaName);

        DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
        updateFavoriteButton(holder.addToFavoritesButton, dbHelper.isFavoritePizza(userEmail, pizzaName));

        holder.addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = dbHelper.isFavoritePizza(userEmail, pizzaName);
                if (isFavorite) {
                    dbHelper.removeFavoritePizza(userEmail, pizzaName);
                } else {
                    dbHelper.addFavoritePizza(userEmail, pizzaName);
                }
                updateFavoriteButton(holder.addToFavoritesButton, !isFavorite);
                listener.onAddToFavoritesClick(pizzaName);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPizzaLayoutClick(pizza);
            }
        });

        holder.orderButton.setOnClickListener(v -> listener.onOrderButtonClick(pizzaName, userEmail));
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    public void updateList(List<ContentValues> newList) {
        pizzaList = newList;
        notifyDataSetChanged();
    }

    private void updateFavoriteButton(ImageButton button, boolean isFavorite) {
        button.setImageResource(isFavorite ? R.drawable.ic_filled_add_24 : R.drawable.ic_empty_add_24);
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaNameTextView;
        ImageButton addToFavoritesButton;
        Button orderButton;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            addToFavoritesButton = itemView.findViewById(R.id.btn_add_to_favorites);
            orderButton = itemView.findViewById(R.id.btn_order);
        }
    }

    public interface OnPizzaClickListener {
        void onAddToFavoritesClick(String pizzaName);
        void onPizzaLayoutClick(ContentValues pizza);
        void onOrderButtonClick(String pizzaName, String userEmail);
    }
}
