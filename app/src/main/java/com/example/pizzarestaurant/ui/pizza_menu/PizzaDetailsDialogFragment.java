// PizzaDetailsDialogFragment.java
package com.example.pizzarestaurant.ui.pizza_menu;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.pizzarestaurant.R;

public class PizzaDetailsDialogFragment extends DialogFragment {

    private static final String ARG_PIZZA_DETAILS = "pizza_details";

    public static PizzaDetailsDialogFragment newInstance(ContentValues pizza) {
        PizzaDetailsDialogFragment fragment = new PizzaDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PIZZA_DETAILS, pizza);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pizza_details, container, false);

        if (getArguments() != null) {
            ContentValues pizza = getArguments().getParcelable(ARG_PIZZA_DETAILS);
            if (pizza != null) {
                TextView pizzaNameTextView = view.findViewById(R.id.pizzaNameTextView);
                TextView pizzaDescriptionTextView = view.findViewById(R.id.pizzaDescriptionTextView);
                TextView pizzaPriceTextView = view.findViewById(R.id.pizzaPriceTextView);
                TextView pizzaSizeTextView = view.findViewById(R.id.pizzaSizeTextView);

                pizzaNameTextView.setText(pizza.getAsString("pizza_name"));
                pizzaDescriptionTextView.setText(String.format("Description: %s", pizza.getAsString("description")));
                pizzaPriceTextView.setText(String.format("Price: %s", String.valueOf(pizza.getAsDouble("price"))));
                pizzaSizeTextView.setText(String.format("Size: %s", pizza.getAsString("size")));
            }
        }

        return view;
    }
}
