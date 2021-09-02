package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private Button login, signup;
    private TextView forgot;
    private ImageView pass_toggle;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //initializing views. we have declared in xml files. see res/layout/login for xml layout file
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signupbtn);
        forgot = findViewById(R.id.forgotPass);
        pass_toggle = findViewById(R.id.pass_toggle);
        mAuth = FirebaseAuth.getInstance();

        //initialing progress dialog
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);


        //login button onclick listener. when user press login button then the written below will be executed.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation for email
                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }
                //validating for valid email i.e entered email contains @ and a .
                if (!isValidEmail(email.getText().toString().trim())) {
                    email.setError("Invalid Email");
                    email.requestFocus();
                    return;
                }
                //validating password
                if (pass.getText().toString().trim().isEmpty()) {
                    pass.setError("Required");
                    pass.requestFocus();
                    return;
                }

                pd.show();

                //firebase sign-in with email and password starts here.
                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(current.getUid());
                                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Intent it = new Intent(LoginActivity.this, ShowMeetings.class);
                                            startActivity(it);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            }
                        });
            }
        });


        //forgot password button will move the user to forgot passwrod activity
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(it);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(it);
            }
        });


        //in password field there is a password show hide button below it will show or hide the passwrod.
        pass_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    pass_toggle.setImageResource(R.drawable.password_hide);

                    //Show Password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                } else {
                    pass_toggle.setImageResource(R.drawable.password_show);
                    //Hide Password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                }

            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
