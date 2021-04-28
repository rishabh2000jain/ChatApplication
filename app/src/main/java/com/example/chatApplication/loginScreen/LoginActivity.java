package com.example.chatApplication.loginScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.chatApplication.R;
import com.example.chatApplication.sharedPreferences.SharedPrefClass;
import com.example.chatApplication.chatScreen.ChatActivity;
import com.example.chatApplication.databinding.ActivityLoginBinding;
import com.example.chatApplication.registerScreen.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth firebaseAuth;
    private SharedPrefClass sharedPrefClass;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPrefClass = SharedPrefClass.getInstance(this);
        //if user is already logged in
        if (sharedPrefClass.isUserLoggedIn()) {
            moveToActivity(ChatActivity.class,true);
        }
        activityLoginBinding.setLoginButton(this);
        activityLoginBinding.setNewUserRegister(this);
    }

    public void onLoginButtonClick() {
        String username = activityLoginBinding.userEmail.getText().toString();
        String password = activityLoginBinding.userPassword.getText().toString();
        if (username.isEmpty()) {
            activityLoginBinding.userEmail.setError("can't be empty");
        } else if (password.isEmpty()) {
            activityLoginBinding.userPassword.setError("can't be empty");
        } else {
            activityLoginBinding.loginProgress.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            moveToActivity(ChatActivity.class,true);
                            sharedPrefClass.setLogin(firebaseAuth.getCurrentUser().getEmail());
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        activityLoginBinding.loginProgress.setVisibility(View.GONE);
                    });
        }


    }

    public void onRegisterTextClick() {
        moveToActivity(RegisterActivity.class,false);
    }

    public void moveToActivity(Class<?> tClass,boolean clearStack) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
        if(clearStack){
            finish();
        }
    }
}