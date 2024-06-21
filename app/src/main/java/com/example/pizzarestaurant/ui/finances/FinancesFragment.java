package com.example.pizzarestaurant.ui.finances;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pizzarestaurant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinancesFragment extends Fragment {

    private FinancesViewModel mViewModel;
    private RecyclerView financesRecyclerView;
    private FinancesAdapter financesAdapter;
    private TextView totalIncomeTextView;

    public static FinancesFragment newInstance() {
        return new FinancesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finances, container, false);
        financesRecyclerView = view.findViewById(R.id.financesRecyclerView);
        totalIncomeTextView = view.findViewById(R.id.totalIncomeTextView);
        financesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FinancesViewModel.class);

        mViewModel.loadFinances(getContext());

        mViewModel.getPizzaOrderCounts().observe(getViewLifecycleOwner(), new Observer<Map<String, Integer>>() {
            @Override
            public void onChanged(Map<String, Integer> pizzaOrderCounts) {
                updateFinancesList();
            }
        });

        mViewModel.getPizzaIncomes().observe(getViewLifecycleOwner(), new Observer<Map<String, Double>>() {
            @Override
            public void onChanged(Map<String, Double> pizzaIncomes) {
                updateFinancesList();
            }
        });

        mViewModel.getTotalIncome().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalIncome) {
                updateTotalIncome(totalIncome);
            }
        });
    }

    private void updateFinancesList() {
        Map<String, Integer> orderCounts = mViewModel.getPizzaOrderCounts().getValue();
        Map<String, Double> incomes = mViewModel.getPizzaIncomes().getValue();

        if (orderCounts != null && incomes != null) {
            List<FinanceItem> financeItems = new ArrayList<>();
            for (String pizzaName : orderCounts.keySet()) {
                financeItems.add(new FinanceItem(pizzaName, orderCounts.get(pizzaName), incomes.get(pizzaName)));
            }

            financesAdapter = new FinancesAdapter(financeItems);
            financesRecyclerView.setAdapter(financesAdapter);
        }
    }

    private void updateTotalIncome(Double totalIncome) {
        if (totalIncome != null) {
            totalIncomeTextView.setText(String.format("$%.2f", totalIncome));
        }
    }
}
