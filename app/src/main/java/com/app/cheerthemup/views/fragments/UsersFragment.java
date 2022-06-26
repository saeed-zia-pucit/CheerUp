package com.app.cheerthemup.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.cheerthemup.views.activities.MessageActivity1;
import com.app.cheerthemup.R;
import com.app.cheerthemup.interfaces.ItemClickListener;
import com.app.cheerthemup.models.User;
import com.app.cheerthemup.adapters.UsersAdapter;
import com.app.cheerthemup.notifications.Token;
import com.app.cheerthemup.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;


public class UsersFragment extends Fragment {
    private static final String TAG = "ProfileFragment";


    //views
    RecyclerView recyclerView;


    //adapter
    UsersAdapter usersAdapter;


    //firebase
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextView txtToolbarTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        txtToolbarTitle = getActivity().findViewById(R.id.txt_toolbar_title);

        txtToolbarTitle.setText("Users");

        recyclerView = view.findViewById(R.id.recyclerview_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        getUsersData();
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");

        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);


    }


    private void getUsersData() {

        ArrayList<User> users = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                    } else {

                        users.add(user);
                    }
                }


                usersAdapter = new UsersAdapter(users);
                recyclerView.setAdapter(usersAdapter);


                usersAdapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int pos, String userName) {

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor edit = prefs.edit();


//                        boolean recieverIdentityHidden = users.get(pos).isIdentityHidden();
                        Intent intent = new Intent(getActivity(), MessageActivity1.class);
                        String uid = users.get(pos).getUid();
//                        edit.putString(Constants.USERNAME, userName);
//                        edit.putString(Constants.fromNotification, "false");
                        intent.putExtra(Constants.USERNAME, userName);
                        intent.putExtra(Constants.UID, uid);
                        intent.putExtra(Constants.fromNotification, "false");
                        //for not from notification
//                        intent.putExtra(Constants.RECIEVERIDENTITYHIDDEN, String.valueOf(recieverIdentityHidden));
                        //for from notification
//                        edit.putString(Constants.RECIEVERIDENTITYHIDDEN, String.valueOf(recieverIdentityHidden));
//                        edit.apply();
                        startActivity(intent);
                    }
                });
                Log.d(TAG, "onDataChange: " + users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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