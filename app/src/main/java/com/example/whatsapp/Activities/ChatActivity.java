package com.example.whatsapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsapp.Adapters.MessagesAdapter;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);
        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecycler.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        database.getReference()
                .child("Chats")
                .child(senderRoom)
                .child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.sendMsg.getText().toString();
                if (message.isEmpty()){
                    binding.sendMsg.setError("Type a message!!!");
                } else {

                    Date date = new Date();
                    Message message1 = new Message(message, senderUid, date.getTime());

                    String randomKey = database.getReference().push().getKey();

                    HashMap<String, Object> lastMsgObj = new HashMap<>();
                    lastMsgObj.put("lastMsg", message1.getMessage());
                    lastMsgObj.put("lastMsgTime", date.getTime());
                    database.getReference().child("Chats").child(senderRoom).updateChildren(lastMsgObj);
                    database.getReference().child("Chats").child(receiverRoom).updateChildren(lastMsgObj);


                    database.getReference()
                            .child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .child(randomKey)
                            .setValue(message1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.sendMsg.setText("");
                            database.getReference()
                                    .child("Chats")
                                    .child(receiverRoom)
                                    .child("Messages")
                                    .child(randomKey)
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