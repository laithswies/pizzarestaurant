// PizzaMenuFragment.java
package com.example.pizzarestaurant.ui.pizza_menu;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.LoginActivity;
import com.example.pizzarestaurant.R;
import com.example.pizzarestaurant.databinding.FragmentPizzaMenuBinding;

import java.util.List;
import java.util.stream.Collectors;

public class PizzaMenuFragment extends Fragment implements PizzaAdapter.OnPizzaClickListener {

    private FragmentPizzaMenuBinding binding;
    private PizzaMenuViewModel pizzaMenuViewModel;
    private PizzaAdapter adapter;
    private String userEmail;
    private List<ContentValues> allPizzas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userEmail = LoginActivity.sharedEmail;
        pizzaMenuViewModel = new ViewModelProvider(this).get(PizzaMenuViewModel.class);
        binding = FragmentPizzaMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewPizzas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Spinner spinner = root.findViewById(R.id.spinner_filter_options);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Button btnFilter = root.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter(spinner.getSelectedItem().toString());
            }
        });

        pizzaMenuViewModel.getPizzaTypes().observe(getViewLifecycleOwner(), new Observer<List<ContentValues>>() {
            @Override
            public void onChanged(List<ContentValues> pizzaTypes) {
                allPizzas = pizzaTypes;
                setAdapter(pizzaTypes);
            }
        });

        return root;
    }

    private void setAdapter(List<ContentValues> pizzaTypes) {
        adapter = new PizzaAdapter(pizzaTypes, PizzaMenuFragment.this, userEmail);
        binding.recyclerViewPizzas.setAdapter(adapter);
    }

    @Override
    public void onAddToFavoritesClick(String pizzaName) {

    }

    @Override
    public void onOrderButtonClick(String pizzaName, String userEmail) {
        OrderDialog orderDialog = new OrderDialog(pizzaName, userEmail);
        orderDialog.show(getParentFragmentManager(), "OrderDialog");
    }


    private void applyFilter(String filterOption) {
        List<ContentValues> filteredList = allPizzas;
        switch (filterOption) {
            case "Show All":
                // No filter applied, display all pizzas
                filteredList = allPizzas;
                break;
            case "Price Ascending":
                filteredList = allPizzas.stream()
                        .sorted((p1, p2) -> Double.compare(p1.getAsDouble("price"), p2.getAsDouble("price")))
                        .collect(Collectors.toList());
                break;
            case "Price Descending":
                filteredList = allPizzas.stream()
                        .sorted((p1, p2) -> Double.compare(p2.getAsDouble("price"), p1.getAsDouble("price")))
                        .collect(Collectors.toList());
                break;
            case "Size Small":
                filteredList = allPizzas.stream()
                        .filter(p -> "Small".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Size Medium":
                filteredList = allPizzas.stream()
                        .filter(p -> "Medium".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Size Large":
                filteredList = allPizzas.stream()
                        .filter(p -> "Large".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Category Chicken":
                filteredList = allPizzas.stream()
                        .filter(p -> "Chicken".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Beef":
                filteredList = allPizzas.stream()
                        .filter(p -> "Beef".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Veggies":
                filteredList = allPizzas.stream()
                        .filter(p -> "Veggies".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Others":
                filteredList = allPizzas.stream()
                        .filter(p -> "Others".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
        }
        adapter.updateList(filteredList);
    }

    @Override
    public void onPizzaLayoutClick(ContentValues pizza) {
        // Create an instance of PizzaDetailsDialogFragment and show it
        PizzaDetailsDialogFragment dialogFragment = PizzaDetailsDialogFragment.newInstance(pizza);
        dialogFragment.show(getParentFragmentManager(), "PizzaDetailsDialog");
    }
}
