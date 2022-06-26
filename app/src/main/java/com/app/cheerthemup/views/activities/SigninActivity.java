package com.app.cheerthemup.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.app.cheerthemup.R;
import com.app.cheerthemup.models.User;
import com.app.cheerthemup.interfaces.ForgotPasswordFragmentClickListener;
import com.app.cheerthemup.utils.Constants;
import com.app.cheerthemup.views.fragments.dialogfragments.ForgotPasswordDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity implements ForgotPasswordFragmentClickListener {
    private static final String TAG = "SigninActivity";
    MaterialButton btnLogin;

    MaterialTextView txtJoinNow, txtForgotPass;
    TextInputEditText edtEmail, edtPassword;

    //firebase

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseAuth.getInstance().signOut();
//        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
//        SharedPreferences.Editor edit = sharedPreferences1.edit();
//        edit.clear().apply();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
        boolean contains = sharedPreferences.contains(Constants.EMAIL);


        if (contains) {
            //user already logged in
            if (firebaseAuth != null) {

                if (firebaseAuth.getCurrentUser() != null) {

                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            boolean profileSetupDone = user.isProfileSetupDone();

                            if (contains) {
                                if (profileSetupDone) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));
                                    edit.apply();
                                    startActivity(new Intent(SigninActivity.this, DashboardActivity.class));
                                } else {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));
                                    edit.apply();
                                    startActivity(new Intent(SigninActivity.this, SetupYourProfileActivity.class));
                                }
                            } else {
                                setContentView(R.layout.activity_signin);
                                //firebase
                                firebaseAuth = FirebaseAuth.getInstance();

                                edtEmail = findViewById(R.id.edit_email);
                                edtPassword = findViewById(R.id.edit_password);

                                txtForgotPass = findViewById(R.id.txt_forgot_pass);

                                txtForgotPass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showForgotPasswordDialog();
                                    }
                                });
                                txtJoinNow = findViewById(R.id.txt_joinnow);
                                btnLogin = findViewById(R.id.btn_login);
                                btnLogin.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                startActivity(new Intent(SigninActivity.this,SignupActivity.class));

                                        String email = edtEmail.getText().toString();
                                        String password = edtPassword.getText().toString();
                                        login(email, password);
                                    }
                                });


                                txtJoinNow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(SigninActivity.this, SignupActivity.class));
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        } else {
            setContentView(R.layout.activity_signin);
            //firebase
            firebaseAuth = FirebaseAuth.getInstance();

            edtEmail = findViewById(R.id.edit_email);
            edtPassword = findViewById(R.id.edit_password);

            txtForgotPass = findViewById(R.id.txt_forgot_pass);

            txtForgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showForgotPasswordDialog();
                }
            });
            txtJoinNow = findViewById(R.id.txt_joinnow);
            btnLogin = findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                startActivity(new Intent(SigninActivity.this,SignupActivity.class));

                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();
                    login(email, password);
                }
            });


            txtJoinNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SigninActivity.this, SignupActivity.class));
                }
            });


        }


//User user = new User("ammar","123");
//        FirebaseDatabase.getInstance().getReference("Users").push().setValue(user);

    }

    private void showForgotPasswordDialog() {

        ForgotPasswordDialogFragment forgotPasswordDialogFragment =
                ForgotPasswordDialogFragment.newInstance();
        forgotPasswordDialogFragment.setCancelable(true);
//        Bundle arguments = forgotPasswordDialogFragment.getArguments();

        forgotPasswordDialogFragment.show(getSupportFragmentManager(),
                "forgot_passs_dialog_fragment");
    }


    private void login(String email, String password) {
//        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified(email, password);
                        } else {
                            Toast.makeText(SigninActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SigninActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void checkIfEmailVerified(String email, String password) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser.isEmailVerified()) {

            if (firebaseAuth != null) {

                if (firebaseAuth.getCurrentUser() != null) {

                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                User user = snapshot.getValue(User.class);
                                boolean profileSetupDone = user.isProfileSetupDone();


                                if (profileSetupDone) {
                                    String uid = currentUser.getUid();
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString(Constants.EMAIL, email);
                                    edit.putString(Constants.PASSWORD, password);
                                    edit.putString(Constants.UID, uid);
                                    edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));
                                    edit.apply();

                                    Intent intent = new Intent(SigninActivity.this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    finish();

                                } else {
                                    String uid = currentUser.getUid();
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString(Constants.EMAIL, email);
                                    edit.putString(Constants.PASSWORD, password);
                                    edit.putString(Constants.UID, uid);
                                    edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));

                                    edit.apply();


                                    Intent intent = new Intent(SigninActivity.this, SetupYourProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    finish();


                                }
                            } else {
                                User user = new User("", "", "", "", false, "", false, false);
                                databaseReference.child(currentUser.getUid()).setValue(user);


                                String uid = currentUser.getUid();
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
                                SharedPreferences.Editor edit = sharedPreferences.edit();

                                edit.putString(Constants.EMAIL, email);
                                edit.putString(Constants.PASSWORD, password);
                                edit.putString(Constants.UID, uid);
                                edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));
                                edit.apply();


                                Intent intent = new Intent(SigninActivity.this, SetupYourProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                finish();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }


//            String uid = currentUser.getUid();
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
//            SharedPreferences.Editor edit = sharedPreferences.edit();
//            edit.putString(Constants.EMAIL, email);
//            edit.putString(Constants.PASSWORD, password);
//            edit.putString(Constants.UID, uid);
//            edit.apply();
//            User user = new User(email,password);
//databaseReference.child(uid).setValue(user);


//            Intent intent = new Intent(SigninActivity.this, SetupYourProfileActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
////                            progressBar.setVisibility(View.GONE);
//            finish();

        } else {
            Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public void onClick(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SigninActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SigninActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SigninActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}