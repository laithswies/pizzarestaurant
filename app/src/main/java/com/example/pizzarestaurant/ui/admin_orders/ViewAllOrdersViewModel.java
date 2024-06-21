package com.example.pizzarestaurant.ui.admin_orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import com.example.pizzarestaurant.DatabaseHelper;
import java.util.List;
import android.content.ContentValues;

public class ViewAllOrdersViewModel extends ViewModel {
    private MutableLiveData<List<ContentValues>> allOrders;

    public LiveData<List<ContentValues>> getAllOrders(Context context) {
        if (allOrders == null) {
            allOrders = new MutableLiveData<>();
            loadAllOrders(context);
        }
        return allOrders;
    }

    private void loadAllOrders(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<ContentValues> orderList = dbHelper.getAllOrders();
        allOrders.setValue(orderList);
    }
}
