// OrdersViewModel.java
package com.example.pizzarestaurant.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import com.example.pizzarestaurant.DatabaseHelper;
import java.util.List;
import android.content.ContentValues;

public class OrdersViewModel extends ViewModel {
    private MutableLiveData<List<ContentValues>> orders;

    public LiveData<List<ContentValues>> getOrders(Context context, String email) {
        if (orders == null) {
            orders = new MutableLiveData<>();
            loadOrders(context, email);
        }
        return orders;
    }

    public void refreshOrders(Context context, String email) {
        loadOrders(context, email);
    }

    private void loadOrders(Context context, String email) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<ContentValues> orderList = dbHelper.getOrdersByEmail(email);
        orders.setValue(orderList);
    }
}
