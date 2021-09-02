package com.feedbacks.feedbackcookie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /****** Create Thread that will sleep for 1.5 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent it=new Intent(Splash.this, ShowMeetings.class);
                        startActivity(it);
                        finish();
                    }else{
                        Intent it=new Intent(Splash.this, LoginSelector.class);
                        startActivity(it);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        // start thread
        background.start();
    }
}
