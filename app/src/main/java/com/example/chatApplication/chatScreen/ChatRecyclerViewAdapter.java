package com.example.chatApplication.chatScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatApplication.Constants;
import com.example.chatApplication.R;
import com.example.chatApplication.databinding.ChatViewMeBinding;
import com.example.chatApplication.databinding.ChatViewOtherBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Chat> chats;
    FirebaseAuth auth;
    public ChatRecyclerViewAdapter(Context context,ArrayList<Chat> chats){
        layoutInflater = LayoutInflater.from(context);
        this.chats = chats;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == Constants.MY_MESSAGE) {
             view = layoutInflater.inflate(R.layout.chat_view_me, parent, false);
        }else {
             view = layoutInflater.inflate(R.layout.chat_view_other, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(holder.getItemViewType(),chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats == null ? 0 : chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getEmail().equals(auth.getCurrentUser().getEmail())){
            return Constants.MY_MESSAGE;
        }
        return Constants.OTHER_MESSAGE;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder{


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        void bind(int type,Chat chat){
            if(type == Constants.MY_MESSAGE){
                ChatViewMeBinding chatViewMeBinding = DataBindingUtil.bind(itemView);

                if (chatViewMeBinding != null) {
                    chatViewMeBinding.setChatString(chat.getMessage());
                    chatViewMeBinding.setEmail(chat.getEmail());
                    chatViewMeBinding.userImage.setImageResource(R.drawable.userone);
                }
            }else{
                ChatViewOtherBinding chatViewOtherBinding = DataBindingUtil.bind(itemView);
                if (chatViewOtherBinding != null) {
                    chatViewOtherBinding.setChatString(chat.getMessage());
                    chatViewOtherBinding.setEmail(chat.getEmail());
                    chatViewOtherBinding.userImage.setImageResource(R.drawable.usertwo);
                }
            }
        }
    }
}
