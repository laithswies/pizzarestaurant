// OrderDialog.java
package com.example.pizzarestaurant.ui.pizza_menu;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.R;

import java.util.Date;

public class OrderDialog extends DialogFragment {

    private String pizzaName;
    private String userEmail;
    private double pizzaPrice;
    private String pizzaSize;

    public OrderDialog(String pizzaName, String userEmail) {
        this.pizzaName = pizzaName;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_order);

        TextView sizeTextView = dialog.findViewById(R.id.text_view_size);
        TextView priceTextView = dialog.findViewById(R.id.text_view_price);
        EditText quantityEditText = dialog.findViewById(R.id.edit_text_quantity);
        Button submitButton = dialog.findViewById(R.id.btn_submit_order);

        // Fetch pizza details from the database
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        ContentValues pizza = dbHelper.getPizzaDetails(pizzaName);
        pizzaSize = pizza.getAsString("size");
        pizzaPrice = pizza.getAsDouble("price");

        sizeTextView.setText("Size: " + pizzaSize);
        priceTextView.setText(String.valueOf("Price: " + pizzaPrice));

        // Calculate price based on quantity
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int quantity = Integer.parseInt(s.toString());
                    if (quantity > 0) {
                        double totalPrice = pizzaPrice * quantity;
                        priceTextView.setText(String.format("Price: %s", totalPrice));
                    } else {
                        quantityEditText.setError("Quantity must be at least 1");
                    }
                } else {
                    priceTextView.setText(String.valueOf("Price: " + pizzaPrice));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        submitButton.setOnClickListener(v -> {
            String quantityText = quantityEditText.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                if (quantity > 0) {
                    double totalPrice = pizzaPrice * quantity;

                    // Store the order in the database
                    dbHelper.addOrder(userEmail, pizzaName, new Date(), totalPrice, quantity, pizzaSize);

                    // Show a toast message indicating the order has been added
                    Toast.makeText(getContext(), "Order has been added", Toast.LENGTH_SHORT).show();

                    dismiss();
                } else {
                    quantityEditText.setError("Quantity must be at least 1");
                }
            } else {
                quantityEditText.setError("Please enter quantity");
            }
        });

        return dialog;
    }
}
