// DataBaseHelper.java
package com.example.pizzarestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PizzaApp.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_PIZZAS = "pizzas";
    private static final String TABLE_SPECIAL_OFFERS = "special_offers";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_PIZZA_NAME = "pizza_name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_OFFER_NAME = "offer_name";
    private static final String COLUMN_PIZZA_TYPES = "pizza_types";
    private static final String COLUMN_OFFER_PERIOD = "offer_period";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_PROFILE_PICTURE = "profile_picture";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_EMAIL + " TEXT PRIMARY KEY,"
                + COLUMN_PHONE_NUMBER + " TEXT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_PROFILE_PICTURE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PIZZA_NAME + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "))";
        db.execSQL(CREATE_FAVORITES_TABLE);

        String CREATE_PIZZAS_TABLE = "CREATE TABLE " + TABLE_PIZZAS + "("
                + COLUMN_PIZZA_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_SIZE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_PIZZAS_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE orders ("
                + "email TEXT,"
                + "pizza_name TEXT,"
                + "order_date INTEGER,"
                + "price REAL,"
                + "quantity INTEGER,"
                + "size TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE " + TABLE_SPECIAL_OFFERS + "("
                + COLUMN_OFFER_NAME + " TEXT,"
                + COLUMN_PIZZA_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_SIZE + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_OFFER_NAME + ", " + COLUMN_PIZZA_NAME + "))";
        db.execSQL(CREATE_SPECIAL_OFFERS_TABLE);

        // Insert the admin user
        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_EMAIL, "admin@pizza.com");
        adminValues.put(COLUMN_PASSWORD, hashPassword("admin1234"));
        adminValues.put(COLUMN_ROLE, "admin");
        adminValues.put(COLUMN_FIRST_NAME, "Admin");
        adminValues.put(COLUMN_LAST_NAME, "Admin");
        adminValues.put(COLUMN_PHONE_NUMBER, "0500000000");
        db.insert(TABLE_USERS, null, adminValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_ROLE + " TEXT");

            // Insert the admin user if upgrading from version 1
            ContentValues adminValues = new ContentValues();
            adminValues.put(COLUMN_EMAIL, "admin@pizza.com");
            adminValues.put(COLUMN_PASSWORD, hashPassword("admin1234"));
            adminValues.put(COLUMN_ROLE, "admin");
            db.insert(TABLE_USERS, null, adminValues);
        }
        if (oldVersion < 3) {
            String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PIZZA_NAME + " TEXT,"
                    + "FOREIGN KEY(" + COLUMN_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "))";
            db.execSQL(CREATE_FAVORITES_TABLE);
        }
        if (oldVersion < 4) {
            String CREATE_PIZZAS_TABLE = "CREATE TABLE " + TABLE_PIZZAS + "("
                    + COLUMN_PIZZA_NAME + " TEXT PRIMARY KEY,"
                    + COLUMN_PRICE + " REAL,"
                    + COLUMN_SIZE + " TEXT,"
                    + COLUMN_CATEGORY + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT" + ")";
            db.execSQL(CREATE_PIZZAS_TABLE);
        }
        if (oldVersion < 5) {
            String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE " + TABLE_SPECIAL_OFFERS + "("
                    + COLUMN_OFFER_NAME + " TEXT,"
                    + COLUMN_PIZZA_TYPES + " TEXT,"
                    + COLUMN_OFFER_PERIOD + " TEXT,"
                    + COLUMN_TOTAL_PRICE + " REAL,"
                    + "PRIMARY KEY (" + COLUMN_OFFER_NAME + ", " + COLUMN_PIZZA_TYPES + "))";
            db.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
        }
        if (oldVersion < 6) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIAL_OFFERS);
            String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE " + TABLE_SPECIAL_OFFERS + "("
                    + COLUMN_OFFER_NAME + " TEXT,"
                    + COLUMN_PIZZA_NAME + " TEXT,"
                    + COLUMN_PRICE + " REAL,"
                    + COLUMN_SIZE + " TEXT,"
                    + "PRIMARY KEY (" + COLUMN_OFFER_NAME + ", " + COLUMN_PIZZA_NAME + "))";
            db.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
        }
    }
    public String getUserProfilePicture(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PROFILE_PICTURE}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        String profilePicture = null;
        if (cursor.moveToFirst()) {
            profilePicture = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE));
        }
        cursor.close();
        db.close();
        return profilePicture;
    }

    public boolean updateUserProfilePicture(String email, String profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PICTURE, profilePicture);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    public List<String> getPizzaNames() {
        List<String> pizzaNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PIZZAS, new String[]{COLUMN_PIZZA_NAME}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                pizzaNames.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pizzaNames;
    }

    public void addPizza(String pizzaName, double price, String size, String category, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PIZZA_NAME, pizzaName);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);

        db.insert(TABLE_PIZZAS, null, values);
        db.close();
    }

    public ContentValues getPizzaDetails(String pizzaName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PIZZAS,
                null, // All columns
                COLUMN_PIZZA_NAME + "=?",
                new String[]{pizzaName},
                null, null, null);

        ContentValues pizza = null;
        if (cursor.moveToFirst()) {
            pizza = new ContentValues();
            pizza.put(COLUMN_PIZZA_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
            pizza.put(COLUMN_PRICE, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            pizza.put(COLUMN_SIZE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
            pizza.put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            pizza.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        }
        cursor.close();
        db.close();
        return pizza;
    }

    public void addOrder(String email, String pizzaName, Date orderDate, double price, int quantity, String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("pizza_name", pizzaName);
        values.put("order_date", orderDate.getTime());
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("size", size);

        db.insert("orders", null, values);
        db.close();
    }

    public boolean removeOrder(String email, String pizzaName, long orderDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("orders",
                "email = ? AND pizza_name = ? AND order_date = ?",
                new String[]{email, pizzaName, String.valueOf(orderDate)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean addSpecialOffer(String offerName, String pizzaName, double price, String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_NAME, offerName);
        values.put(COLUMN_PIZZA_NAME, pizzaName);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_SIZE, size);

        long result = db.insert(TABLE_SPECIAL_OFFERS, null, values);
        db.close();
        return result != -1;
    }
    public List<ContentValues> getSpecialOffers() {
        List<ContentValues> specialOffers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SPECIAL_OFFERS, null);

        if (cursor.moveToFirst()) {
            do {
                ContentValues offer = new ContentValues();
                offer.put(COLUMN_OFFER_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OFFER_NAME)));
                offer.put(COLUMN_PIZZA_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
                offer.put(COLUMN_PRICE, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                offer.put(COLUMN_SIZE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
                specialOffers.add(offer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return specialOffers;
    }

    // New method to get the number of orders for each pizza type
    public Map<String, Integer> getPizzaOrderCounts() {
        Map<String, Integer> orderCounts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT pizza_name, SUM(quantity) as total_quantity FROM orders GROUP BY pizza_name";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow("pizza_name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("total_quantity"));
                orderCounts.put(pizzaName, quantity);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Ensure all pizza types are included
        List<String> allPizzas = getPizzaNames();
        for (String pizzaName : allPizzas) {
            orderCounts.putIfAbsent(pizzaName, 0);
        }

        db.close();
        return orderCounts;
    }

    // New method to get the total income for each pizza type
    public Map<String, Double> getPizzaIncomes() {
        Map<String, Double> pizzaIncomes = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT pizza_name, SUM(price * quantity) as total_income FROM orders GROUP BY pizza_name";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow("pizza_name"));
                double income = cursor.getDouble(cursor.getColumnIndexOrThrow("total_income"));
                pizzaIncomes.put(pizzaName, income);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Ensure all pizza types are included
        List<String> allPizzas = getPizzaNames();
        for (String pizzaName : allPizzas) {
            pizzaIncomes.putIfAbsent(pizzaName, 0.0);
        }

        db.close();
        return pizzaIncomes;
    }

    // New method to get the total income for all pizzas
    public double getTotalIncome() {
        double totalIncome = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(price * quantity) as total_income FROM orders";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("total_income"));
        }

        cursor.close();
        db.close();
        return totalIncome;
    }

    // Add this method in DatabaseHelper.java
    public List<ContentValues> getOrdersByEmail(String email) {
        List<ContentValues> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("orders",
                null, // All columns
                "email = ?",
                new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ContentValues order = new ContentValues();
                order.put("email", cursor.getString(cursor.getColumnIndexOrThrow("email")));
                order.put("pizza_name", cursor.getString(cursor.getColumnIndexOrThrow("pizza_name")));
                order.put("order_date", cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.put("price", cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                order.put("quantity", cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                order.put("size", cursor.getString(cursor.getColumnIndexOrThrow("size")));
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }


    public List<ContentValues> getAllPizzas() {
        List<ContentValues> pizzaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PIZZAS, null);

        if (cursor.moveToFirst()) {
            do {
                ContentValues pizza = new ContentValues();
                pizza.put(COLUMN_PIZZA_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
                pizza.put(COLUMN_PRICE, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                pizza.put(COLUMN_SIZE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
                pizza.put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                pizza.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                pizzaList.add(pizza);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pizzaList;
    }

    public boolean addUser(String email, String phoneNumber, String firstName, String lastName, String gender, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, "client"); // Default role is client

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean addAdmin(String email, String phoneNumber, String firstName, String lastName, String gender, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, "admin"); // Default role is admin

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ROLE},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
        }
        cursor.close();
        db.close();
        return role;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_EMAIL, COLUMN_ROLE},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean isValidUser = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValidUser;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean addFavoritePizza(String email, String pizzaName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PIZZA_NAME, pizzaName);

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public List<ContentValues> getFavoritePizzaDetails(String email) {
        List<ContentValues> favoritePizzaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pizzas WHERE pizza_name IN (SELECT pizza_name FROM favorites WHERE email = ?)", new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                ContentValues pizza = new ContentValues();
                pizza.put(COLUMN_PIZZA_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
                pizza.put(COLUMN_PRICE, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                pizza.put(COLUMN_SIZE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
                pizza.put(COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                pizza.put(COLUMN_DESCRIPTION, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                favoritePizzaList.add(pizza);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favoritePizzaList;
    }


    public List<String> getFavoritePizzas(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> favoritePizzas = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{COLUMN_PIZZA_NAME},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                favoritePizzas.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favoritePizzas;
    }

    // Add these methods in DatabaseHelper.java
    public String getUserFirstName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_FIRST_NAME}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        String firstName = null;
        if (cursor.moveToFirst()) {
            firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
        }
        cursor.close();
        db.close();
        return firstName;
    }

    public String getUserLastName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_LAST_NAME}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        String lastName = null;
        if (cursor.moveToFirst()) {
            lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
        }
        cursor.close();
        db.close();
        return lastName;
    }

    public String getUserPhoneNumber(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PHONE_NUMBER}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        String phoneNumber = null;
        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
        }
        cursor.close();
        db.close();
        return phoneNumber;
    }

    public boolean updateUser(String email, String firstName, String lastName, String phoneNumber, String hashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_PASSWORD, hashedPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    public boolean updateUserNoPassword(String email, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUserProfile(String firstName, String lastName, String phoneNumber, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_PASSWORD, password);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{getLoggedInUserEmail()});
        db.close();
        return rowsAffected > 0;
    }

    // This method should return the logged-in user's email. Implement this based on your authentication logic.
    private String getLoggedInUserEmail() {
        // Placeholder for actual logged-in user email retrieval logic
        return LoginActivity.sharedEmail;
    }


    public boolean isFavoritePizza(String userEmail, String pizzaName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("favorites", new String[]{"pizza_name"},
                "email = ? AND pizza_name = ?", new String[]{userEmail, pizzaName},
                null, null, null);

        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isFavorite;
    }

    public boolean removeFavoritePizza(String userEmail, String pizzaName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("favorites",
                "email = ? AND pizza_name = ?", new String[]{userEmail, pizzaName});
        db.close();
        return rowsAffected > 0;
    }

    public List<ContentValues> getAllOrders() {
        List<ContentValues> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("orders",
                null, // All columns
                null, // No selection criteria
                null, // No selection arguments
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                ContentValues order = new ContentValues();
                order.put("email", cursor.getString(cursor.getColumnIndexOrThrow("email")));
                order.put("pizza_name", cursor.getString(cursor.getColumnIndexOrThrow("pizza_name")));
                order.put("order_date", cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.put("price", cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                order.put("quantity", cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                order.put("size", cursor.getString(cursor.getColumnIndexOrThrow("size")));
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }


}
