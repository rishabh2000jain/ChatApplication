package com.example.chatApplication.chatScreen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatApplication.notification.Data;
import com.example.chatApplication.notification.NotificationBody;
import com.example.chatApplication.network.RetrofitHelper;
import com.example.chatApplication.notification.MessageResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivityViewModel extends ViewModel {
    private final DatabaseReference databaseReference;
    private final MutableLiveData<ArrayList<Chat>> listMutableLiveData;
    private final RetrofitHelper.RetrofitService retrofitService;

    public ChatActivityViewModel() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("chats");
        listMutableLiveData = new MutableLiveData<>();
        retrofitService = RetrofitHelper.getInstance();
    }

    public void insertChat(Chat chat) {
        Date date = new Date();
        long time = date.getTime();
        int todayDate = date.getDate();
        int todayMonth = date.getMonth();
        databaseReference.child(todayDate + "" + todayMonth + "" + time).setValue(chat);
        sendNotification(chat);
    }

    private void sendNotification(Chat chat) {
        NotificationBody notificationBody = new NotificationBody();
        Data data = new Data();
        data.setMessage(chat.getMessage());
        data.setTitle(chat.getEmail());
        notificationBody.setData(data);
        retrofitService.sendNotification(notificationBody).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                if (response.body().success == -1) {
                    Log.i("Notification Status", "Fail" + response.code() + "\n" + response.errorBody());
                } else {
                    Log.i("Notification Status", "send " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.i("Notification Status", "" + t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Chat>> getChatList() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Chat> list = new ArrayList<>();
                Iterable<DataSnapshot> snapList = snapshot.getChildren();
                for (DataSnapshot data : snapList) {
                    Chat chat = data.getValue(Chat.class);
                    list.add(chat);
                }
                listMutableLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listMutableLiveData;
    }
}
