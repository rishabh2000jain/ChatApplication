package com.example.chatApplication.network;

import com.example.chatApplication.Constants;
import com.example.chatApplication.notification.NotificationBody;
import com.example.chatApplication.notification.MessageResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class RetrofitHelper {
    private static RetrofitService retrofitService = null;

    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }

    public interface RetrofitService {
        @Headers(
                {
                        "Authorization:key=" + Constants.MESSAGE_SERVER_KEY,
                        "Content-Type:application/json",

                }
        )

        @POST("fcm/send")
        Call<MessageResponse> sendNotification(@Body NotificationBody body);
    }
}
