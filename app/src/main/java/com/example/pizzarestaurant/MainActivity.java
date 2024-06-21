package com.example.pizzarestaurant;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button btnGetStarted;
    private ImageView backgroundImageView;
    private ImageView logoImageView;
    private TextView restaurantNameTextView;
    private static final String PIZZA_URL = "https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/";
    private static final String TAG = "MainActivity";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetStarted = findViewById(R.id.btnGetStarted);
        backgroundImageView = findViewById(R.id.imageView);
        logoImageView = findViewById(R.id.imageView2);
        restaurantNameTextView = findViewById(R.id.textView);
        dbHelper = new DatabaseHelper(this);

        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        // Apply the animation to each view
        backgroundImageView.startAnimation(slideInLeft);
        logoImageView.startAnimation(slideInLeft);
        restaurantNameTextView.startAnimation(slideInLeft);
        btnGetStarted.startAnimation(slideInLeft);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPizzaTypes();
            }
        });
    }

    private void fetchPizzaTypes() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, PIZZA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the "types" array from the response
                            JSONArray typesArray = response.getJSONArray("types");
                            // Add random attributes and save to database
                            for (int i = 0; i < typesArray.length(); i++) {
                                String pizzaName = typesArray.getString(i);
                                addPizzaToDatabase(pizzaName);
                            }
                            // Connection successful, go to login and registration section
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing pizza types", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log error for debugging
                        Log.e(TAG, "Error: " + error.toString());
                        // Connection unsuccessful, show error message
                        Toast.makeText(MainActivity.this, "Error fetching pizza types", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void addPizzaToDatabase(String pizzaName) {
        Random random = new Random();
        String[] sizes = {"Small", "Medium", "Large"};
        String[] categories = {"Chicken", "Beef", "Veggies", "Others"};
        String[] descriptions = {
                "Delicious and mouth-watering.",
                "A classic favorite.",
                "Topped with fresh ingredients.",
                "Perfect for any occasion."
        };

        double price = 5.0 + (20.0 - 5.0) * random.nextDouble();
        price = Math.floor(price);
        String size = sizes[random.nextInt(sizes.length)];
        String category = categories[random.nextInt(categories.length)];
        String description = descriptions[random.nextInt(descriptions.length)];

        dbHelper.addPizza(pizzaName, price, size, category, description);
    }
}
