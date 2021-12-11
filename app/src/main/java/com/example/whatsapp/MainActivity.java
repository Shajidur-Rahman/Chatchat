package com.example.whatsapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Toast.makeText(MainActivity.this, "Loged out", Toast.LENGTH_SHORT).show();
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