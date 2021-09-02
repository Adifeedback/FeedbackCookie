package com.feedbacks.feedbackcookie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button reset;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        reset = findViewById(R.id.resetbtn);

        pd = new ProgressDialog(ForgotPassword.this);
        pd.setMessage("Please wait");
        pd.setCancelable(false);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }
                if (!isValidEmail(email.getText().toString())){
                    email.setError("Invalid Email");
                    email.requestFocus();
                    return;
                }
                isEmailExist(email.getText().toString());
            }
        });

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public boolean isEmailExist(String Enteredemail){

        pd.show();
        FirebaseAuth mauth = FirebaseAuth.getInstance();

        mauth.fetchSignInMethodsForEmail(Enteredemail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }else{
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = email.getText().toString();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassword.this, "Email Sent.", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();


                                        new AlertDialog.Builder(ForgotPassword.this)
                                                .setTitle("Email Sent")
                                                .setMessage("An Email with reset link has been sent to your email. Please Check")

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton("Ok", null)
                                                .show();

                                    }else{
                                        Toast.makeText(ForgotPassword.this, "Some unexpected error occurred.", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }
                            });
                }
            }
        });

        return false;
    }

}
