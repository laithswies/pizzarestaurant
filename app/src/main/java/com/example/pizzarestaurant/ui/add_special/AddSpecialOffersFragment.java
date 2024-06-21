package com.example.pizzarestaurant.ui.add_special;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddSpecialOffersFragment extends Fragment {

    private AddSpecialOffersViewModel mViewModel;
    private EditText offerNameEditText, totalPriceEditText;
    private TextView offerStartDateTextView, offerEndDateTextView;
    private Spinner pizzaTypeSpinner;
    private Button addOfferButton;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    private String selectedPizzaType;
    private String selectedPizzaSize;

    public static AddSpecialOffersFragment newInstance() {
        return new AddSpecialOffersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_special_offers, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddSpecialOffersViewModel.class);

        offerNameEditText = getView().findViewById(R.id.offerNameEditText);
        pizzaTypeSpinner = getView().findViewById(R.id.pizzaTypeSpinner);
        offerStartDateTextView = getView().findViewById(R.id.offerStartDateTextView);
        offerEndDateTextView = getView().findViewById(R.id.offerEndDateTextView);
        totalPriceEditText = getView().findViewById(R.id.totalPriceEditText);
        addOfferButton = getView().findViewById(R.id.addOfferButton);

        loadPizzaTypes();

        offerStartDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateCalendar, offerStartDateTextView);
            }
        });

        offerEndDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateCalendar, offerEndDateTextView);
            }
        });

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String offerName = offerNameEditText.getText().toString();
                String startDate = offerStartDateTextView.getText().toString();
                String endDate = offerEndDateTextView.getText().toString();
                String totalPriceStr = totalPriceEditText.getText().toString();

                if (offerName.isEmpty() || selectedPizzaType == null || startDate.isEmpty() || endDate.isEmpty() || totalPriceStr.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double totalPrice;
                try {
                    totalPrice = Double.parseDouble(totalPriceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Please enter a valid price", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startDateCalendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    Toast.makeText(getContext(), "Start date cannot be in the past", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (endDateCalendar.getTimeInMillis() < startDateCalendar.getTimeInMillis()) {
                    Toast.makeText(getContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                String offerPeriod = startDate + " to " + endDate;

                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                boolean success = dbHelper.addSpecialOffer(offerName, selectedPizzaType, totalPrice, selectedPizzaSize);

                if (success) {
                    Toast.makeText(getContext(), "Special offer added successfully", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Failed to add special offer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPizzaTypes() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<String> pizzaNames = dbHelper.getPizzaNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pizzaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);

        pizzaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPizzaType = pizzaNames.get(position);
                fetchPizzaSize(selectedPizzaType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPizzaType = null;
                selectedPizzaSize = null;
            }
        });
    }

    private void fetchPizzaSize(String pizzaName) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        ContentValues pizzaDetails = dbHelper.getPizzaDetails(pizzaName);
        if (pizzaDetails != null) {
            selectedPizzaSize = pizzaDetails.getAsString("size");
        } else {
            selectedPizzaSize = null;
        }
    }

    private void showDatePickerDialog(final Calendar calendar, final TextView dateTextView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar, dateTextView);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateLabel(Calendar calendar, TextView dateTextView) {
        String myFormat = "yyyy-MM-dd"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTextView.setText(sdf.format(calendar.getTime()));
    }
}
