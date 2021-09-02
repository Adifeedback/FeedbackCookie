package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuestActivity extends AppCompatActivity {

    EditText meetingId;
    Button join;
    ProgressDialog pd;
    ArrayList<Meetings> list = new ArrayList<>();
    boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        meetingId = findViewById(R.id.meeting_id);
        join = findViewById(R.id.join);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meetingId.getText().toString().trim().isEmpty()){
                    meetingId.setError("Required");
                    meetingId.requestFocus();
                    return;
                }

                pd.show();


                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings");
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Meetings meetings = ds.getValue(Meetings.class);
                            if (meetings != null){
                                if (meetings.getId().equals(meetingId.getText().toString())){
                                    found = true;
                                    Intent it = new Intent(GuestActivity.this, FeedbacksActivity.class);
                                    it.putExtra("id", meetingId.getText().toString());
                                    startActivity(it);
                                }
                            }
                        }
                        if (!found){
                            Toast.makeText(GuestActivity.this, "No meeting found with entered meeting ID.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}
