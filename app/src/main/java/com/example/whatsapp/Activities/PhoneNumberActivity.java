package com.example.whatsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(PhoneNumberActivity.this, MainActivity.class));
            finish();
        }

        binding.phoneNumber.requestFocus();

        binding.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneNumberActivity.this, OtpActivity.class);
                intent.putExtra("phone_number", binding.phoneNumber.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }
}