package com.feedbacks.feedbackcookie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginSelector extends AppCompatActivity {

    Button login, guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selector);

        login = findViewById(R.id.loginbtn);
        guest = findViewById(R.id.guestLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginSelector.this, LoginActivity.class);
                startActivity(it);
            }
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginSelector.this, GuestActivity.class);
                startActivity(it);
            }
        });
    }
}
