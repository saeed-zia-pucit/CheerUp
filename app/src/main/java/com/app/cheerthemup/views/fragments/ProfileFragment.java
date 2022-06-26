package com.app.cheerthemup.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cheerthemup.R;
import com.app.cheerthemup.models.User;
import com.app.cheerthemup.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //views
    TextView txtToolbarTitle, txtUserEmail, txtUserPass, txtUserPhone, txtUserName;
    CheckBox checkBoxIdentityShowHidden;
    LinearLayout linearLayoutIdentity;
    //firebase
    DatabaseReference databaseReference;

    SharedPreferences prefs;

    SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUserEmail = view.findViewById(R.id.txt_user_email);
        txtUserPhone = view.findViewById(R.id.txt_user_phone);
        txtUserName = view.findViewById(R.id.txt_user_name);
        checkBoxIdentityShowHidden = view.findViewById(R.id.checkbox_hide_show_identity);
        linearLayoutIdentity = view.findViewById(R.id.lin_identity);

        txtToolbarTitle = getActivity().findViewById(R.id.txt_toolbar_title);

        txtToolbarTitle.setText("Profile");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        edit = prefs.edit();
        String uid = prefs.getString(Constants.UID, "");


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: " + snapshot);
                User user = snapshot.getValue(User.class);

                txtUserEmail.setText(user.getEmail());
                txtUserName.setText(user.getUserName());
                txtUserPhone.setText(user.getPhone());

                if (user.isPaidUser()) {
                    linearLayoutIdentity.setVisibility(View.VISIBLE);
                    if (user.isIdentityHidden()) {
                        checkBoxIdentityShowHidden.setChecked(true);
                        edit.putBoolean(Constants.USERIDENTITY, true);
                        edit.apply();
                    } else {
                        checkBoxIdentityShowHidden.setChecked(false);
                        edit.putBoolean(Constants.USERIDENTITY, false);
                        edit.apply();
                    }
                } else {
                    linearLayoutIdentity.setVisibility(View.GONE);
                }
//                checkBoxIdentityShowHidden.setChecked(user.isPaidUser()? user.isIdentityHidden());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkBoxIdentityShowHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: ");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);
                        if (isChecked) {
                            user.setIdentityHidden(true);
                            edit.putBoolean(Constants.USERIDENTITY, true);
                            edit.apply();
                            databaseReference.setValue(user);
                        } else {
                            user.setIdentityHidden(false);
                            edit.putBoolean(Constants.USERIDENTITY, false);
                            edit.apply();
                            databaseReference.setValue(user);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Profile");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Profile");
    }
}