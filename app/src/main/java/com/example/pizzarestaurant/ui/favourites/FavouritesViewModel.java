package com.example.pizzarestaurant.ui.favourites;

import android.app.Application;
import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.LoginActivity;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ContentValues>> favoritePizzas;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favoritePizzas = new MutableLiveData<>();
        fetchFavoritePizzas(application);
    }

    public LiveData<List<ContentValues>> getFavoritePizzas() {
        return favoritePizzas;
    }

    private void fetchFavoritePizzas(Application application) {
        DatabaseHelper dbHelper = new DatabaseHelper(application);
        List<ContentValues> favorites = dbHelper.getFavoritePizzaDetails(LoginActivity.sharedEmail);
        favoritePizzas.setValue(favorites);
    }
}
