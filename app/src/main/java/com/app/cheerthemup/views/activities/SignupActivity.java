package com.app.cheerthemup.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.cheerthemup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SignupActivity extends AppCompatActivity {

    MaterialTextView txtSignin;
    MaterialButton btnSignup;
    TextInputEditText edtUsername, edtEmail, edtPassword, edtPhone;

    //firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();


//        edtUsername = findViewById(R.id.edit_username);
        edtEmail = findViewById(R.id.edit_email);
        edtPassword = findViewById(R.id.edit_password);
//        edtPhone = findViewById(R.id.edit_phone);

        btnSignup = findViewById(R.id.btn_signup);
        txtSignin = findViewById(R.id.txt_signin);

        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String userName = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
//                String phone = edtPhone.getText().toString();

//                verifyEmail(email,password,phone,userName);
                register(email, password);

            }
        });


    }

    private void register(String email, String password) {
//        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            verifyEmail(email,password);
//                            authStateListener = new FirebaseAuth.AuthStateListener() {
//                                @Override
//                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//                                    if (currentUser != null) {
//                                        verifyEmail();
//                                    }else{
//                                        //user is signet out
//
//                                    }
//                                }
//                            };


                        } else {
                            Toast.makeText(SignupActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void verifyEmail(String email,String password) {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Email sent", Toast.LENGTH_SHORT).show();


                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignupActivity.this,SigninActivity.class));
                            finish();

                        }else{
                            //email not sent
                            Toast.makeText(SignupActivity.this, "Email not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}