package com.example.pizzarestaurant.ui.view_special_offers;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurant.LoginActivity;
import com.example.pizzarestaurant.R;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.SpecialOfferViewHolder> {

    private List<ContentValues> specialOffers;
    private Fragment fragment;

    public SpecialOffersAdapter(List<ContentValues> specialOffers, Fragment fragment) {
        this.specialOffers = specialOffers;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_special_offer, parent, false);
        return new SpecialOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferViewHolder holder, int position) {
        ContentValues offer = specialOffers.get(position);
        String pizzaName = offer.getAsString("pizza_name");
        double price = offer.getAsDouble("price");
        String size = offer.getAsString("size");

        holder.pizzaNameTextView.setText(pizzaName);
        holder.priceTextView.setText(String.format("Price: %s", price));

        holder.orderButton.setOnClickListener(v -> {
            SpecialOfferOrderDialog orderDialog = new SpecialOfferOrderDialog(pizzaName, LoginActivity.sharedEmail, price, size);
            orderDialog.show(fragment.getParentFragmentManager(), "SpecialOfferOrderDialog");
        });
        holder.itemView.setOnClickListener(v -> {
            SpecialOfferDetailsDialogFragment dialogFragment = SpecialOfferDetailsDialogFragment.newInstance(offer);
            dialogFragment.show(fragment.getParentFragmentManager(), "SpecialOfferDetailsDialog");
        });
    }

    @Override
    public int getItemCount() {
        return specialOffers.size();
    }

    static class SpecialOfferViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaNameTextView;
        TextView priceTextView;
        Button orderButton;

        public SpecialOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            orderButton = itemView.findViewById(R.id.orderButton);
        }
    }
}
