package com.example.pizzarestaurant.ui.orders;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pizzarestaurant.LoginActivity;
import com.example.pizzarestaurant.R;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.widget.TextView;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    private OrdersAdapter adapter;
    private TextView noOrdersTextView;
    private RecyclerView recyclerView;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        Context context = getContext();
        String email = LoginActivity.sharedEmail;

        recyclerView = getView().findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        noOrdersTextView = getView().findViewById(R.id.noOrdersTextView);

        mViewModel.getOrders(context, email).observe(getViewLifecycleOwner(), new Observer<List<ContentValues>>() {
            @Override
            public void onChanged(List<ContentValues> orders) {
                if (orders == null || orders.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noOrdersTextView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noOrdersTextView.setVisibility(View.GONE);
                    if (adapter == null) {
                        adapter = new OrdersAdapter(context, orders);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateOrders(orders);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel != null) {
            String email = LoginActivity.sharedEmail;
            Context context = getContext();
            mViewModel.refreshOrders(context, email);
        }
    }
}
