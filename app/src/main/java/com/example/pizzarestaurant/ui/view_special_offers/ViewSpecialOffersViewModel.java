// ViewSpecialOffersViewModel.java
package com.example.pizzarestaurant.ui.view_special_offers;

import android.app.Application;
import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pizzarestaurant.DatabaseHelper;

import java.util.List;

public class ViewSpecialOffersViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ContentValues>> specialOffers;

    public ViewSpecialOffersViewModel(@NonNull Application application) {
        super(application);
        specialOffers = new MutableLiveData<>();
        fetchSpecialOffers(application);
    }

    public LiveData<List<ContentValues>> getSpecialOffers() {
        return specialOffers;
    }

    private void fetchSpecialOffers(Application application) {
        DatabaseHelper dbHelper = new DatabaseHelper(application);
        List<ContentValues> offers = dbHelper.getSpecialOffers();
        specialOffers.setValue(offers);
    }
}
