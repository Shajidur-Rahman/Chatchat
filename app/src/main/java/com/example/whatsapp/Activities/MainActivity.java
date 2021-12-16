package com.example.whatsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.R;
import com.example.whatsapp.Adapters.UserAdapter;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Users> users;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        users = new ArrayList<>();
        userAdapter = new UserAdapter(this, users);
        binding.recyclerView.setAdapter(userAdapter);

        database.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Users user = snapshot1.getValue(Users.class);
                            if (user.getUid().equals(auth.getCurrentUser().getUid())){
                                Log.d("Shajidur", "onDataChange: no");
                            }
                            else {
                                Log.d("Shajidur", "onDataChange: yessdfdsf");
                                users.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                auth.signOut();
                Toast.makeText(MainActivity.this, "Loged out", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, PhoneNumberActivity.class));
                        finishAffinity();
                    }
                }, 500);
                    break;
            case R.id.group:
                Toast.makeText(MainActivity.this, "Group clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.invite:
                Toast.makeText(MainActivity.this, "Invite", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toppermenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}