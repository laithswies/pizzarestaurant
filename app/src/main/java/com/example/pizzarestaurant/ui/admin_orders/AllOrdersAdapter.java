package com.example.pizzarestaurant.ui.admin_orders;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.R;
import java.util.List;
import android.content.ContentValues;
import android.widget.Toast;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.OrderViewHolder> {

    private List<ContentValues> orderList;
    private Context context;

    public AllOrdersAdapter(Context context, List<ContentValues> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_orders, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        ContentValues order = orderList.get(position);
        holder.pizzaNameTextView.setText(order.getAsString("pizza_name"));
        holder.userNameTextView.setText(order.getAsString("email"));
        holder.orderDateTextView.setText(new java.util.Date(order.getAsLong("order_date")).toString());

        holder.itemView.setOnClickListener(v -> {
            // Display order details
            String details = "Pizza: " + order.getAsString("pizza_name") + "\n" +
                    "User: " + order.getAsString("email") + "\n" +
                    "Date: " + new java.util.Date(order.getAsLong("order_date")).toString() + "\n" +
                    "Price: " + order.getAsDouble("price") + "\n" +
                    "Quantity: " + order.getAsInteger("quantity") + "\n" +
                    "Size: " + order.getAsString("size");

            new AlertDialog.Builder(context)
                    .setTitle("Order Details")
                    .setMessage(details)
                    .setPositiveButton("OK", null)
                    .show();
        });

        holder.removeOrderButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Order")
                    .setMessage("Are you sure you want to remove this order?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        removeOrder(holder.getAdapterPosition());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void removeOrder(int position) {
        if (position >= 0 && position < orderList.size()) {
            ContentValues order = orderList.get(position);
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            String email = order.getAsString("email");
            String pizzaName = order.getAsString("pizza_name");
            long orderDate = order.getAsLong("order_date");

            if (dbHelper.removeOrder(email, pizzaName, orderDate)) {
                orderList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Order removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove order", Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView pizzaNameTextView;
        TextView userNameTextView;
        TextView orderDateTextView;
        ImageButton removeOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaNameTextView = itemView.findViewById(R.id.pizzaNameTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            removeOrderButton = itemView.findViewById(R.id.removeOrderButton);
        }
    }
}
