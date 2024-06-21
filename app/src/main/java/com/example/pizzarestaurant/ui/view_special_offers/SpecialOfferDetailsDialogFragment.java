// SpecialOfferDetailsDialogFragment.java
package com.example.pizzarestaurant.ui.view_special_offers;

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

public class SpecialOfferDetailsDialogFragment extends DialogFragment {

    private static final String ARG_OFFER_DETAILS = "offer_details";

    public static SpecialOfferDetailsDialogFragment newInstance(ContentValues offer) {
        SpecialOfferDetailsDialogFragment fragment = new SpecialOfferDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_OFFER_DETAILS, offer);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_special_offer_details, container, false);

        if (getArguments() != null) {
            ContentValues offer = getArguments().getParcelable(ARG_OFFER_DETAILS);
            if (offer != null) {
                TextView offerNameTextView = view.findViewById(R.id.offerNameTextView);
                TextView pizzaNameTextView = view.findViewById(R.id.pizzaNameTextView);
                TextView priceTextView = view.findViewById(R.id.priceTextView);
                TextView sizeTextView = view.findViewById(R.id.sizeTextView);

                offerNameTextView.setText(offer.getAsString("offer_name"));
                pizzaNameTextView.setText(offer.getAsString("pizza_name"));
                priceTextView.setText(String.format("Price: %s", String.valueOf(offer.getAsDouble("price"))));
                sizeTextView.setText(String.format("Size: %s", offer.getAsString("size")));
            }
        }

        return view;
    }
}
