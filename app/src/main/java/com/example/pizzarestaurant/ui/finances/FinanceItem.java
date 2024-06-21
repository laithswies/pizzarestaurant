package com.example.pizzarestaurant.ui.finances;

public class FinanceItem {
    private final String pizzaName;
    private final int orderCount;
    private final double income;

    public FinanceItem(String pizzaName, int orderCount, double income) {
        this.pizzaName = pizzaName;
        this.orderCount = orderCount;
        this.income = income;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public double getIncome() {
        return income;
    }
}
