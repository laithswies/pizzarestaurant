package com.example.pizzarestaurant.ui.favourites;

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
import com.example.pizzarestaurant.databinding.FragmentFavouritesBinding;
import com.example.pizzarestaurant.ui.pizza_menu.OrderDialog;
import com.example.pizzarestaurant.ui.pizza_menu.PizzaDetailsDialogFragment;

import java.util.List;
import java.util.stream.Collectors;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.OnFavoriteClickListener {

    private FragmentFavouritesBinding binding;
    private FavouritesViewModel favouritesViewModel;
    private FavouritesAdapter adapter;
    private String userEmail;
    private List<ContentValues> allFavorites;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userEmail = LoginActivity.sharedEmail;
        favouritesViewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewFavorites;
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

        favouritesViewModel.getFavoritePizzas().observe(getViewLifecycleOwner(), new Observer<List<ContentValues>>() {
            @Override
            public void onChanged(List<ContentValues> favoritePizzas) {
                allFavorites = favoritePizzas;
                setAdapter(favoritePizzas);
            }
        });

        return root;
    }

    private void setAdapter(List<ContentValues> favoritePizzas) {
        adapter = new FavouritesAdapter(favoritePizzas, FavouritesFragment.this, userEmail);
        binding.recyclerViewFavorites.setAdapter(adapter);
    }

    @Override
    public void onRemoveFromFavoritesClick(String pizzaName) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        dbHelper.removeFavoritePizza(userEmail, pizzaName);
        List<ContentValues> updatedFavorites = dbHelper.getFavoritePizzaDetails(userEmail);
        setAdapter(updatedFavorites);
    }

    @Override
    public void onPizzaLayoutClick(ContentValues pizza) {
        // Create an instance of PizzaDetailsDialogFragment and show it
        PizzaDetailsDialogFragment dialogFragment = PizzaDetailsDialogFragment.newInstance(pizza);
        dialogFragment.show(getParentFragmentManager(), "PizzaDetailsDialog");
    }

    @Override
    public void onOrderButtonClick(String pizzaName, String userEmail) {
        OrderDialog orderDialog = new OrderDialog(pizzaName, userEmail);
        orderDialog.show(getParentFragmentManager(), "OrderDialog");
    }

    private void applyFilter(String filterOption) {
        List<ContentValues> filteredList = allFavorites;
        switch (filterOption) {
            case "Price Ascending":
                filteredList = allFavorites.stream()
                        .sorted((p1, p2) -> Double.compare(p1.getAsDouble("price"), p2.getAsDouble("price")))
                        .collect(Collectors.toList());
                break;
            case "Price Descending":
                filteredList = allFavorites.stream()
                        .sorted((p1, p2) -> Double.compare(p2.getAsDouble("price"), p1.getAsDouble("price")))
                        .collect(Collectors.toList());
                break;
            case "Size Small":
                filteredList = allFavorites.stream()
                        .filter(p -> "Small".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Size Medium":
                filteredList = allFavorites.stream()
                        .filter(p -> "Medium".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Size Large":
                filteredList = allFavorites.stream()
                        .filter(p -> "Large".equalsIgnoreCase(p.getAsString("size")))
                        .collect(Collectors.toList());
                break;
            case "Category Chicken":
                filteredList = allFavorites.stream()
                        .filter(p -> "Chicken".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Beef":
                filteredList = allFavorites.stream()
                        .filter(p -> "Beef".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Veggies":
                filteredList = allFavorites.stream()
                        .filter(p -> "Veggies".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
            case "Category Others":
                filteredList = allFavorites.stream()
                        .filter(p -> "Others".equalsIgnoreCase(p.getAsString("category")))
                        .collect(Collectors.toList());
                break;
        }
        adapter.updateList(filteredList);
    }
}
