package com.app.cheerthemup.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.cheerthemup.R;
import com.app.cheerthemup.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetupYourProfileActivity extends AppCompatActivity {

    TextInputEditText edtUserName, edtEmail, edtPhone;
    MaterialButton btnSetupProfile, btnSkip;


    //firebase

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_your_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        edtUserName = findViewById(R.id.edit_username);
        edtEmail = findViewById(R.id.edit_email);
        edtPhone = findViewById(R.id.edit_phone);

        btnSkip = findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("", "", "","", false,"",false,false);
                skipThisStep(user);
            }
        });

        String email = firebaseAuth.getCurrentUser().getEmail();
        edtEmail.setText(email);
        btnSetupProfile = findViewById(R.id.btn_setup_my_profile);
        btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString();
//                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();


//                User user = new User(userName, email, phone, true,firebaseAuth.getCurrentUser().getUid());
//                User user = new User(userName, "Anonymous",email, phone, true,firebaseAuth.getCurrentUser().getUid(),false);

                User user = new User(userName,"Anonymous",email,phone,true,firebaseAuth.getCurrentUser().getUid(),false,false);

                setupMyProfile(user);
            }
        });
    }

    private void skipThisStep(User user) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.setValue(user);


    }

    private void setupMyProfile(User user) {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.setValue(user);

        startActivity(new Intent(SetupYourProfileActivity.this, DashboardActivity.class));

    }
}