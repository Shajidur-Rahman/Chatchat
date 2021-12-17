package com.example.whatsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Adapters.TopStatusAdapter;
import com.example.whatsapp.Adapters.UserAdapter;
import com.example.whatsapp.Models.Status;
import com.example.whatsapp.Models.UserStatus;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Users> users;
    UserAdapter userAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image");
        dialog.setCancelable(false);

        userStatuses = new ArrayList<>();
        statusAdapter = new TopStatusAdapter(this, userStatuses);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        users = new ArrayList<>();
        userAdapter = new UserAdapter(this, users);

        database.getReference().child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusRecycler.setLayoutManager(layoutManager);
        binding.statusRecycler.setAdapter(statusAdapter);
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

        database.getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            userStatuses.clear();
                            for (DataSnapshot storiesSnap : snapshot.getChildren()){
                                UserStatus userStatus = new UserStatus();
                                userStatus.setName(storiesSnap.child("name").getValue(String.class));
                                userStatus.setProfileImage(storiesSnap.child("profileImage").getValue(String.class));
                                userStatus.setLastUploaded(storiesSnap.child("lastUpdate").getValue(Long.class));

                                ArrayList<Status> statuses = new ArrayList<>();

                                for (DataSnapshot story : storiesSnap.child("status").getChildren()){
                                    Status smaple = story.getValue(Status.class);
                                    statuses.add(smaple);
                                }

                                userStatus.setStatuses(statuses);

                                userStatuses.add(userStatus);
                            }
                            statusAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 75);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null){
            if (data.getData() != null){
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //////
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUploaded(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdate", userStatus.getLastUploaded());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUploaded());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("status")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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