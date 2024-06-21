package com.example.pizzarestaurant.ui.finances;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzarestaurant.R;

import java.util.List;

public class FinancesAdapter extends RecyclerView.Adapter<FinancesAdapter.FinancesViewHolder> {

    private final List<FinanceItem> financeItems;

    public FinancesAdapter(List<FinanceItem> financeItems) {
        this.financeItems = financeItems;
    }

    @NonNull
    @Override
    public FinancesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finances, parent, false);
        return new FinancesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancesViewHolder holder, int position) {
        FinanceItem item = financeItems.get(position);
        holder.pizzaNameTextView.setText(item.getPizzaName());
        holder.pizzaOrderCountTextView.setText(String.valueOf(item.getOrderCount()));
        holder.pizzaIncomeTextView.setText(String.format("$%.2f", item.getIncome()));
    }

    @Override
    public int getItemCount() {
        return financeItems.size();
    }

    static class FinancesViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaNameTextView;
        TextView pizzaOrderCountTextView;
        TextView pizzaIncomeTextView;

        public FinancesViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            pizzaOrderCountTextView = itemView.findViewById(R.id.pizzaOrderCountTextView);
            pizzaIncomeTextView = itemView.findViewById(R.id.pizzaIncomeTextView);
        }
    }
}
