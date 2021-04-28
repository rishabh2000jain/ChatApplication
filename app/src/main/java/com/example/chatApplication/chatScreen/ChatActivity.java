package com.example.chatApplication.chatScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatApplication.BuildConfig;
import com.example.chatApplication.Constants;
import com.example.chatApplication.R;
import com.example.chatApplication.sharedPreferences.SharedPrefClass;
import com.example.chatApplication.databinding.ActivityChatBinding;
import com.example.chatApplication.loginScreen.LoginActivity;
import com.example.chatApplication.notification.FirebaseNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private ActivityChatBinding activityChatBinding;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private ChatActivityViewModel chatActivityViewModel;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Chat> list;
    SharedPrefClass sharedPrefClass;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseNotification.isForeGrounded = true;
        sharedPrefClass = SharedPrefClass.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();
        //if the firebase user is logged out due to some reasons
        //and the firebaseAuth.getCurrentUser().getEmail() give email of other user present logged in
        if (!firebaseAuth.getCurrentUser().getEmail().equals(sharedPrefClass.getCurrentUserEmail())) {
            sharedPrefClass.setLogout();
            moveToLogin();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //generating notification only when the activity is in foreground
        FirebaseNotification.isForeGrounded = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatActivityViewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        if (BuildConfig.FLAVOR.equals("paid")) {
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);
        }
        bindRecyclerView(list);
        chatActivityViewModel.getChatList().observe(this, chats -> {
            if (activityChatBinding.chatProgress.getVisibility() == View.VISIBLE) {
                activityChatBinding.chatProgress.setVisibility(View.GONE);
            }
            int currSize = list.size();
            int newSize = chats.size();
            if (newSize - currSize > 0) {
                for (int i = currSize; i < newSize; i++) {
                    list.add(chats.get(i));
                }
            }
            chatRecyclerViewAdapter.notifyDataSetChanged();
            // to avoid -ve index on 0 size of list
            int pos = chats.size() == 0 ? 0 : chats.size() - 1;
            activityChatBinding.recyclerView.smoothScrollToPosition(pos);
        });
        activityChatBinding.setSendButton(this);
        activityChatBinding.toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
}

    public void buttonSend() {
        String chatString = activityChatBinding.chatText.getText().toString();
        if (!chatString.isEmpty()) {
            Chat chat = new Chat(chatString, sharedPrefClass.getCurrentUserEmail());
            chatActivityViewModel.insertChat(chat);
            chatRecyclerViewAdapter.notifyDataSetChanged();
            activityChatBinding.chatText.setText("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_activity_menu, menu);
        return true;
    }

    //logout button click
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SharedPrefClass sharedPrefClass = SharedPrefClass.getInstance(getApplicationContext());
        sharedPrefClass.setLogout();
        FirebaseAuth.getInstance().signOut();
        moveToLogin();
        return true;
    }


    private void bindRecyclerView(ArrayList<Chat> list) {
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(this, list);
        activityChatBinding.recyclerView.setAdapter(chatRecyclerViewAdapter);
        activityChatBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void moveToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}