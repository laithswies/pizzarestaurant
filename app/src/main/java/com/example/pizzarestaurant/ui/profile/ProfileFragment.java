package com.example.pizzarestaurant.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pizzarestaurant.R;
import com.example.pizzarestaurant.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProfileViewModel mViewModel;
    private TextInputEditText etFirstName, etLastName, etPhoneNumber, etPassword, etConfirmPassword;
    private Button btnSave, btnChangePass, btnChangePicture;
    private ImageView profilePicture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePicture = view.findViewById(R.id.profilePicture);
        btnChangePicture = view.findViewById(R.id.btnChangePicture);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnSave = view.findViewById(R.id.btnSave);
        btnChangePass = view.findViewById(R.id.changePassBtn);

        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getVisibility() == View.INVISIBLE) {
                    etPassword.setVisibility(View.VISIBLE);
                    etConfirmPassword.setVisibility(View.VISIBLE);
                    btnChangePass.setText("Don't Change Password");
                } else {
                    etPassword.setVisibility(View.INVISIBLE);
                    etConfirmPassword.setVisibility(View.INVISIBLE);
                    btnChangePass.setText("Change Password");
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.loadProfileData(getContext(), etFirstName, etLastName, etPhoneNumber, profilePicture);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                profilePicture.setImageBitmap(bitmap);
                saveProfilePicture(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfilePicture(Bitmap bitmap) {
        String encodedImage = encodeToBase64(bitmap);
        mViewModel.saveProfilePicture(getContext(), encodedImage);

        // Broadcast an intent to notify that the profile picture has been updated
        Intent intent = new Intent("com.example.pizzarestaurant.PROFILE_PICTURE_UPDATED");
        getContext().sendBroadcast(intent);
    }

    private String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void saveProfileChanges() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (firstName.length() < 3) {
            etFirstName.setError("First name must be at least 3 characters");
            return;
        }

        if (lastName.length() < 3) {
            etLastName.setError("Last name must be at least 3 characters");
            return;
        }

        if (!phoneNumber.matches("^05\\d{8}$")) {
            etPhoneNumber.setError("Phone number must be exactly 10 digits starting with 05");
            return;
        }

        if (!password.isEmpty()) {
            if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
                etPassword.setError("Password must be at least 8 characters and include at least 1 letter and 1 number");
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }
        }

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        String hashedPassword = hashPassword(password);
        boolean isUpdated;
        if (password.isEmpty()) {
            isUpdated = mViewModel.updateUserProfileNoPassword(getContext(), firstName, lastName, phoneNumber);
        } else {
            isUpdated = mViewModel.updateUserProfile(getContext(), firstName, lastName, phoneNumber, hashedPassword);
        }
        if (isUpdated) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Restart the parent activity
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
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
}
