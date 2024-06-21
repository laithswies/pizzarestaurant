//PizzaMenuViewModel.java
package com.example.pizzarestaurant.ui.pizza_menu;

import android.app.Application;
import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pizzarestaurant.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class PizzaMenuViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ContentValues>> pizzaTypes;

    public PizzaMenuViewModel(@NonNull Application application) {
        super(application);
        pizzaTypes = new MutableLiveData<>();
        fetchPizzaTypes(application);
    }

    public LiveData<List<ContentValues>> getPizzaTypes() {
        return pizzaTypes;
    }

    private void fetchPizzaTypes(Application application) {
        DatabaseHelper dbHelper = new DatabaseHelper(application);
        List<ContentValues> pizzas = dbHelper.getAllPizzas();
        pizzaTypes.setValue(pizzas);
    }
}
