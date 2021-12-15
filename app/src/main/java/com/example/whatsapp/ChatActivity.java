package com.example.whatsapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    MessagesAdapter adapter;
    ArrayList<Message> messages;
    ActivityChatBinding binding;
    String senderRoom, receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages);

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.sendMsg.getText().toString();
                if (message.isEmpty()){
                    binding.sendMsg.setError("Type a message!!!");
                } else {

                    Date date = new Date();
                    Message message1 = new Message(message, senderUid, date.getTime());
                    database.getReference()
                            .child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .setValue(message1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.sendMsg.setText("");
                            database.getReference()
                                    .child("Chats")
                                    .child(receiverRoom)
                                    .child("Messages")
                                    .setValue(message1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    });
                }
            }
        });

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}