package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ShowMeetings extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView logout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meetings);

        recyclerView = findViewById(R.id.questionsRv);
        logout = findViewById(R.id.logout);
        fab = findViewById(R.id.addMeetings);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            logout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent it = new Intent(ShowMeetings.this, LoginSelector.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShowMeetings.this, AddMeetings.class);
                startActivity(it);
            }
        });


    }

    public void loadRv(){

        ArrayList<Meetings> list = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Meetings meetings = ds.getValue(Meetings.class);
                    list.add(meetings);
                }

                Collections.reverse(list);

                recyclerView.setLayoutManager(new LinearLayoutManager(ShowMeetings.this));
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    recyclerView.setAdapter(new QuestionAdapter(list, "yes"));
                }else{
                    recyclerView.setAdapter(new QuestionAdapter(list, "no"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadRv();
    }
}
