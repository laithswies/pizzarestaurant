package com.example.pizzarestaurant.ui.add_admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pizzarestaurant.DatabaseHelper;
import com.example.pizzarestaurant.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddAdminFragment extends Fragment {

    private AddAdminViewModel mViewModel;
    private EditText etEmail, etPhoneNumber, etFirstName, etLastName, etPassword, etConfirmPassword;
    private Spinner spGender;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    public static AddAdminFragment newInstance() {
        return new AddAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddAdminViewModel.class);

        etEmail = view.findViewById(R.id.etEmail);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        spGender = view.findViewById(R.id.spGender);
        btnRegister = view.findViewById(R.id.btnRegister);

        dbHelper = new DatabaseHelper(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAdmin();
            }
        });
    }

    private void registerAdmin() {
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String gender = spGender.getSelectedItem().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (email.isEmpty() || phoneNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = hashPassword(password);

        // assuming your DatabaseHelper has a method to add an admin
        boolean isInserted = dbHelper.addAdmin(email, phoneNumber, firstName, lastName, gender, hashedPassword);

        if (isInserted) {
            Toast.makeText(getContext(), "Admin registered successfully", Toast.LENGTH_SHORT).show();
            // clear fields after successful registration
            etEmail.setText("");
            etPhoneNumber.setText("");
            etFirstName.setText("");
            etLastName.setText("");
            etPassword.setText("");
            etConfirmPassword.setText("");
            spGender.setSelection(0);
        } else {
            Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
