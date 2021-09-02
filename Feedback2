package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMeetings extends AppCompatActivity {

    EditText title;
    Button save;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meetings);

        title = findViewById(R.id.meetingTitle);
        save = findViewById(R.id.saveBtn);

        pd = new ProgressDialog(AddMeetings.this);
        pd.setMessage("Saving...");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().trim().isEmpty()){
                    title.setError("Required");
                    title.requestFocus();
                    return;
                }

                pd.show();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings");
                Meetings meetings = new Meetings();
                meetings.setTitle(title.getText().toString());
                meetings.setId(dbRef.push().getKey());

                dbRef.child(meetings.getId()).setValue(meetings).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(AddMeetings.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddMeetings.this, "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
