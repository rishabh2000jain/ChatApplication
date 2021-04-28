package com.example.chatApplication.registerScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatApplication.R;
import com.example.chatApplication.databinding.ActivityRegisterBinding;
import com.example.chatApplication.loginScreen.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding activityRegisterBinding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        activityRegisterBinding.setRegisterButton(this);
    }

    public void onRegisterButtonClick() {
        String username = activityRegisterBinding.userEmail.getText().toString();
        String password = activityRegisterBinding.userPassword.getText().toString();
        if (username.isEmpty()) {
            activityRegisterBinding.userEmail.setError("username can't be empty");
        } else if (password.isEmpty()) {
            activityRegisterBinding.userPassword.setError("username can't be empty");
        } else if(password.length() < 6){
            Toast.makeText(getApplicationContext(),"password length should be at least 6",Toast.LENGTH_SHORT).show();
        }else {
            activityRegisterBinding.registerProgress.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        moveToLoginActivity();
                    } else {
                        Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                    activityRegisterBinding.registerProgress.setVisibility(View.GONE);
                }
            });
        }
    }

    public void moveToLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}