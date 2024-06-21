package com.example.pizzarestaurant.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.LoginActivity;

public class ProfileViewModel extends ViewModel {

    public void loadProfileData(Context context, EditText etFirstName, EditText etLastName, EditText etPhoneNumber, ImageView profilePicture) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = LoginActivity.sharedEmail;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        String firstName = dbHelper.getUserFirstName(email);
        String lastName = dbHelper.getUserLastName(email);
        String phoneNumber = dbHelper.getUserPhoneNumber(email);
        String encodedImage = dbHelper.getUserProfilePicture(email);

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etPhoneNumber.setText(phoneNumber);

        if (encodedImage != null) {
            Bitmap bitmap = decodeBase64(encodedImage);
            profilePicture.setImageBitmap(bitmap);
        }
    }

    public boolean updateUserProfileNoPassword(Context context, String firstName, String lastName, String phoneNumber) {
        String email = LoginActivity.sharedEmail;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.updateUserNoPassword(email, firstName, lastName, phoneNumber);
    }

    public boolean updateUserProfile(Context context, String firstName, String lastName, String phoneNumber, String hashedPassword) {
        String email = LoginActivity.sharedEmail;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.updateUser(email, firstName, lastName, phoneNumber, hashedPassword);
    }

    public void saveProfilePicture(Context context, String encodedImage) {
        String email = LoginActivity.sharedEmail;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.updateUserProfilePicture(email, encodedImage);
    }

    private Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
