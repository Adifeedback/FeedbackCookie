package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class FeedbacksActivity extends AppCompatActivity {

    String id;
    RecyclerView recyclerView;
    EditText feedback;
    ImageView sendFeedback, copy, export;

    ArrayList<Meetings> list = new ArrayList<>();
    String body = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_feedbacks);

        id = getIntent().getStringExtra("id");
        recyclerView = findViewById(R.id.feedbacksRv);
        feedback = findViewById(R.id.feedback);
        sendFeedback = findViewById(R.id.saveBtn);
        copy = findViewById(R.id.copy_id_icon);
        export = findViewById(R.id.export_icon);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            copy.setVisibility(View.VISIBLE);
            export.setVisibility(View.VISIBLE);
        }

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", id);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(FeedbacksActivity.this, "Meeting ID Copied.", Toast.LENGTH_SHORT).show();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()){
                    for (int i = 0; i < list.size(); i++){
                        body = body + list.get(i).getTitle()+ "\n\n";
                    }
                    if (checkPermission()){
                        generateTxtFile(FeedbacksActivity.this, id, body);
                    }else{
                        requestPermission();
                    }
                }
                else{
                    Toast.makeText(FeedbacksActivity.this, "There is nothing in the feedbacks", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedback.getText().toString().trim().isEmpty()){
                    feedback.setError("Required");
                    feedback.requestFocus();
                    return;
                }

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings").child(id).child("Feedbacks");
                Meetings meetings = new Meetings();
                meetings.setTitle(feedback.getText().toString());
                meetings.setId(dbRef.push().getKey());

                dbRef.child(meetings.getId()).setValue(meetings).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(FeedbacksActivity.this, "Feedback Saved.", Toast.LENGTH_SHORT).show();
                            feedback.getText().clear();
                            loadRv();
                        }else{
                            Toast.makeText(FeedbacksActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadRv();
    }

    public void loadRv() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Meetings").child(id).child("Feedbacks");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Meetings meetings = ds.getValue(Meetings.class);
                    list.add(meetings);
                }

                Collections.reverse(list);

                recyclerView.setLayoutManager(new LinearLayoutManager(FeedbacksActivity.this));
                recyclerView.setAdapter(new QuestionAdapter(list, "no"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generateTxtFile(Context context, String sFileName, String sBody) {

        try {
            String root = Environment.getExternalStorageDirectory() + "/" + "FeedbackCookie/";
            // Create the parent path
            File dir = new File(root);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File gpxfile = new File(root, sFileName + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    generateTxtFile(FeedbacksActivity.this, id, body);
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 99:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        generateTxtFile(FeedbacksActivity.this, id, body);
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(FeedbacksActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(FeedbacksActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(FeedbacksActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 99);
        }
    }
}
