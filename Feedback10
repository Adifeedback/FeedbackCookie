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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, confirm_password;
    Button signup;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirm_password = findViewById(R.id.c_pass);
        signup = findViewById(R.id.signupbtn);

        pd = new ProgressDialog(this);
        pd.setMessage("Signing In...");

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }

                if (password.getText().toString().trim().isEmpty()){
                    password.setError("Required");
                    password.requestFocus();
                    return;
                }
                if (confirm_password.getText().toString().trim().isEmpty()){
                    confirm_password.setError("Required");
                    confirm_password.requestFocus();
                    return;
                }
                if (!password.getText().toString().trim().equals(
                        confirm_password.getText().toString().trim()
                )){
                    confirm_password.setError("Password does not matched.");
                    confirm_password.requestFocus();
                    return;
                }

                pd.show();

                //creating user
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    finish();
                                } else {
                                    Exception e = task.getException();
                                    Toast.makeText(SignUpActivity.this, e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }
                        });
            }
        });

    }
}
