package com.example.carparkingapp.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carparkingapp.R;
import com.example.carparkingapp.google_map.DashboardActivity;
import com.example.carparkingapp.login.LoginActivity;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etPhoneNo, etAddress;
    TextView tvHeading, tv2, tv3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et1);
        etEmail = findViewById(R.id.et2);
        etPassword = findViewById(R.id.et3);
        etPhoneNo = findViewById(R.id.et4);
        etAddress = findViewById(R.id.et5);
        Button btnRegister = findViewById(R.id.b1);
        tvHeading = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        btnRegister.setOnClickListener(v -> registerUser());

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneText = etPhoneNo.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneText.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        long phoneNo;
        try {
            phoneNo = Long.parseLong(phoneText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User object
        User user = new User(name, email, password, phoneNo, address);

        // API Call without Authorization header
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(this).create(ApiInterface.class);
        Call<User> call = apiInterface.postUser(user);  // No token needed

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Success: User registered!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}