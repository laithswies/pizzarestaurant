package com.example.pizzarestaurant.ui.admin_orders;

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
import com.example.pizzarestaurant.R;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.widget.TextView;

public class ViewAllOrdersFragment extends Fragment {

    private ViewAllOrdersViewModel mViewModel;
    private AllOrdersAdapter adapter;
    private TextView noOrdersTextView;

    public static ViewAllOrdersFragment newInstance() {
        return new ViewAllOrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ViewAllOrdersViewModel.class);

        Context context = getContext();
        RecyclerView recyclerView = getView().findViewById(R.id.allOrdersRecyclerView);
        noOrdersTextView = getView().findViewById(R.id.noOrdersTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mViewModel.getAllOrders(context).observe(getViewLifecycleOwner(), new Observer<List<ContentValues>>() {
            @Override
            public void onChanged(List<ContentValues> orders) {
                if (orders == null || orders.isEmpty()) {
                    noOrdersTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noOrdersTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (adapter == null) {
                        adapter = new AllOrdersAdapter(context, orders);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
