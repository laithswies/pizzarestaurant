package com.example.pizzarestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{5,}$";
    private static final String PHONE_PATTERN = "^05\\d{8}$";
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern patternPhone = Pattern.compile(PHONE_PATTERN);
    private static boolean emailValid = false;
    private static boolean firstNameValid = false;
    private static boolean lastNameValid = false;
    private static boolean passwordValid = false;
    private static boolean confPasswordValid = false;
    private static boolean phoneValid = false;
    private TextInputEditText etEmail, etPhoneNumber, etFirstName, etLastName, etPassword, etConfirmPassword;
    private Spinner spGender;
    private Button btnRegister;
    private ImageView backBtn;
    private DatabaseHelper dbHelper;
    private TextInputLayout layoutEmail, layoutFirstName, layoutPhone, layoutConfPassword, layoutLastName, layoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper(this);
        layoutEmail = findViewById(R.id.textInputLayoutEmail);
        etEmail = findViewById(R.id.etEmail);
        layoutFirstName = findViewById(R.id.textInputLayoutFirstName);
        etFirstName = findViewById(R.id.etFirstName);
        layoutLastName = findViewById(R.id.textInputLayoutLastName);
        etLastName = findViewById(R.id.etLastName);
        layoutPhone = findViewById(R.id.textInputLayoutNumber);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        layoutPassword = findViewById(R.id.textInputLayoutPassword);
        etPassword = findViewById(R.id.etPassword);
        layoutConfPassword = findViewById(R.id.textInputLayoutComPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        backBtn = findViewById(R.id.backButton);

        spGender = findViewById(R.id.spGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidEmail(editable)) {
                    String newEmail = Objects.requireNonNull(etEmail.getText()).toString();
                    if (dbHelper.checkEmailExists(newEmail)) {
                        layoutEmail.setError("This email already exists");
                        emailValid = false;
                    } else {
                        layoutEmail.setError(null);
                        emailValid = true;
                    }
                } else {
                    layoutEmail.setError("Enter in this format test@admin.com");
                    emailValid = false;
                }
            }
        });

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!validateInputLength(editable)) {
                    layoutFirstName.setError("Not less than 3 characters for First Name");
                    firstNameValid = false;
                } else {
                    layoutFirstName.setError(null);
                    firstNameValid = true;
                }
            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!validateInputLength(editable)) {
                    layoutLastName.setError("Not less than 3 characters for Last Name");
                    lastNameValid = false;
                } else {
                    layoutLastName.setError(null);
                    lastNameValid = true;
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String password = Objects.requireNonNull(editable).toString();
                if (!validatePassword(password)) {
                    layoutPassword.setError("Password must be at least 8 characters and include at least 1 letter and 1 number");
                    passwordValid = false;
                } else {
                    layoutPassword.setError(null);
                    passwordValid = true;
                }
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!validateConfPassword(editable, etPassword)) {
                    layoutConfPassword.setError("Passwords must match");
                    confPasswordValid = false;
                } else {
                    layoutConfPassword.setError(null);
                    confPasswordValid = true;
                }
            }
        });

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidPhone(editable)) {
                    layoutPhone.setError("Phone number must be exactly 10 digits starting with 05");
                    phoneValid = false;
                } else {
                    layoutPhone.setError(null);
                    phoneValid = true;
                }
            }
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String gender = spGender.getSelectedItem().toString().trim();

            if (emailValid && firstNameValid && lastNameValid && passwordValid && confPasswordValid && phoneValid) {
                if (dbHelper.addUser(email, phone, firstName, lastName, gender, hashPassword(password))) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputLength(Editable editable) {
        return editable.length() >= 3;
    }

    private boolean validatePassword(String password) {
        return passwordPattern.matcher(password).matches();
    }

    private boolean validateConfPassword(Editable editable, TextInputEditText passwordEditText) {
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();
        return editable.toString().equals(password);
    }

    public static boolean isValidEmail(Editable email) {
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPhone(Editable phone) {
        return patternPhone.matcher(phone).matches();
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
