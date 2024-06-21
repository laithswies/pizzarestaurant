package com.example.pizzarestaurant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pizzarestaurant.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private BroadcastReceiver profilePictureReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdmin.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Load and set the profile picture, name, and email
        View headerView = navigationView.getHeaderView(0);
        ImageView profilePic = headerView.findViewById(R.id.profile_pic);
        TextView profileName = headerView.findViewById(R.id.profile_name);
        TextView profileEmail = headerView.findViewById(R.id.profile_email);
        loadProfileData(profilePic, profileName, profileEmail);

        // Register BroadcastReceiver to update profile picture
        profilePictureReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadProfileData(profilePic, profileName, profileEmail);
            }
        };
        IntentFilter filter = new IntentFilter("com.example.pizzarestaurant.PROFILE_PICTURE_UPDATED");
        LocalBroadcastManager.getInstance(this).registerReceiver(profilePictureReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (profilePictureReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(profilePictureReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadProfileData(ImageView profilePic, TextView profileName, TextView profileEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String email = LoginActivity.sharedEmail; // Get the logged-in user's email
        String encodedImage = dbHelper.getUserProfilePicture(email);
        String firstName = dbHelper.getUserFirstName(email);
        String lastName = dbHelper.getUserLastName(email);

        profileEmail.setText(email);
        profileName.setText(firstName + " " + lastName);

        if (encodedImage != null) {
            Bitmap bitmap = decodeBase64(encodedImage);
            profilePic.setImageBitmap(bitmap);
        }
    }

    private Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
