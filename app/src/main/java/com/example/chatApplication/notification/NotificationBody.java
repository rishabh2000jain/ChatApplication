package com.example.chatApplication.notification;

public class NotificationBody {
    private  String to = "/topics/login";
    private Data data;

    public Data getData() {
        return data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

