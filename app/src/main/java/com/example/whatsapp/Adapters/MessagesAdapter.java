package com.example.whatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ItemReceiveBinding;
import com.example.whatsapp.databinding.SentActivityBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.sent_activity, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiveVIewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
//        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        int reaction[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reaction)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (holder.getClass() == SentViewHolder.class){
                SentViewHolder viewHolder = (SentViewHolder) holder;

                viewHolder.binding.feelings.setImageResource(reaction[pos]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);


//                try{
//                    viewHolder.binding.feelings.setImageResource(reaction[pos]);
//                    viewHolder.binding.feelings.setVisibility(View.VISIBLE);
//                } catch (Exception e){
//                    pos = -1;
//                }


            }
            else {
                ReceiveVIewHolder viewHolder = (ReceiveVIewHolder) holder;

                viewHolder.binding.feelings.setImageResource(reaction[pos]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);


//                try{
//                    viewHolder.binding.feelings.setImageResource(reaction[pos]);
//                    viewHolder.binding.feelings.setVisibility(View.VISIBLE);
//                } catch (Exception e){
//                    pos = -1;
//                }


            }

            message.setImoje(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("Chats")
                    .child(senderRoom)
                    .child("Messages")
                    .child(message.getMessageId())
                    .setValue(message);

                        FirebaseDatabase.getInstance().getReference()
                    .child("Chats")
                    .child(receiverRoom)
                    .child("Messages")
                    .child(message.getMessageId())
                    .setValue(message);



            return true; // true is closing popup, false is requesting a new selection
        });

        if (holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder)holder;

            if(message.getMessage().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholdre)
                        .into(viewHolder.binding.image);
            }

            viewHolder.binding.message.setText(message.getMessage());



            if (message.getImoje() >= 0){
//                message.setImoje(reaction[(int) message.getImoje()]);
                viewHolder.binding.feelings.setImageResource(reaction[message.getImoje()]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feelings.setVisibility(View.GONE);
            }

//            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });

        }
        else {
            ReceiveVIewHolder viewHolder = (ReceiveVIewHolder) holder;

            if(message.getMessage().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholdre)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());




            if (message.getImoje() >= 0){
//                message.setImoje(reaction[(int) message.getImoje()]);
                viewHolder.binding.feelings.setImageResource(reaction[message.getImoje()]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feelings.setVisibility(View.GONE);
            }

//            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
//                @SuppressLint("ClickableViewAccessibility")
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{

        SentActivityBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SentActivityBinding.bind(itemView);
        }
    }

    public class ReceiveVIewHolder extends RecyclerView.ViewHolder{

        ItemReceiveBinding binding;
        public ReceiveVIewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }

}
