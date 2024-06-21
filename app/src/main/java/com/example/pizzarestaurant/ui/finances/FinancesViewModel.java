package com.example.pizzarestaurant.ui.finances;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pizzarestaurant.DatabaseHelper;

import android.content.Context;

import java.util.Map;

public class FinancesViewModel extends ViewModel {
    private MutableLiveData<Map<String, Integer>> pizzaOrderCounts;
    private MutableLiveData<Map<String, Double>> pizzaIncomes;
    private MutableLiveData<Double> totalIncome;

    public void loadFinances(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        pizzaOrderCounts = new MutableLiveData<>(dbHelper.getPizzaOrderCounts());
        pizzaIncomes = new MutableLiveData<>(dbHelper.getPizzaIncomes());
        totalIncome = new MutableLiveData<>(dbHelper.getTotalIncome());
    }

    public LiveData<Map<String, Integer>> getPizzaOrderCounts() {
        return pizzaOrderCounts;
    }

    public LiveData<Map<String, Double>> getPizzaIncomes() {
        return pizzaIncomes;
    }

    public LiveData<Double> getTotalIncome() {
        return totalIncome;
    }
}
