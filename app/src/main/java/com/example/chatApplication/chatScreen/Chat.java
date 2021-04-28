package com.example.chatApplication.chatScreen;

public class Chat {
    String message;
    String email;
    public Chat(){}
    public Chat(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}
