package com.app.cheerthemup.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cheerthemup.R;
import com.app.cheerthemup.models.Message;
import com.app.cheerthemup.models.User;
import com.app.cheerthemup.notifications.Client;
import com.app.cheerthemup.notifications.Data;
import com.app.cheerthemup.notifications.MyResponse;
import com.app.cheerthemup.notifications.Sender;
import com.app.cheerthemup.notifications.Token;
import com.app.cheerthemup.notifications.api.APIService;
import com.app.cheerthemup.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.time.Duration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    MaterialButton btnSend, btnUpgradeToPremium;
    TextInputEditText edtMsg;
    TextView txtMsg;
    Toolbar toolbar;
    LinearLayout linPremium;
    RelativeLayout relativeLayoutMsg;
    CheckBox checkBoxIdentityShowHidden;
    LinearLayout linearLayoutIdentity;


    SharedPreferences prefs;

    SharedPreferences.Editor edit;

    //firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference chatDatabaseReference;


    //fcm api
    public static final String BASE_URL = "https://fcm.googleapis.com/";
    APIService apiService;
    boolean notify = false;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = prefs.edit();
        String uid = prefs.getString(Constants.UID, "");



        checkBoxIdentityShowHidden = findViewById(R.id.checkbox_hide_show_identity);
        linearLayoutIdentity = findViewById(R.id.lin_identity);
        apiService = Client.getClient(BASE_URL).create(APIService.class);

        relativeLayoutMsg = findViewById(R.id.rel_msg);
        linPremium = findViewById(R.id.lin_premium);
        firebaseAuth = FirebaseAuth.getInstance();
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat");

        toolbar = findViewById(R.id.toolbar);

        //intent

        btnUpgradeToPremium = findViewById(R.id.btn_upgrade_to_premium);
        btnSend = findViewById(R.id.btn_send);
        edtMsg = findViewById(R.id.edit_msg);
        txtMsg = findViewById(R.id.txt_msg);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = prefs.edit();

        if (prefs.getString(Constants.fromNotification, "").equals("true")) {
/////////               FROM NOTIFICATION        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);
            String userStatus = sharedPreferences.getString(Constants.USERSTATUS, "");

            checkUserStatus(userStatus);

            String reciverIdentity = getIntent().getStringExtra(Constants.RECIEVERIDENTITYHIDDEN);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            String userName = getIntent().getExtras().getString(Constants.USERNAME);
            String[] split = userName.split(":");


            setUserNameOnToolbar(userStatus, reciverIdentity, toolbar, split[0]);


            btnUpgradeToPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upgradeToPremium();
                }
            });


            hideAndShowLayoutIdentity(uid);
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    User user = snapshot.getValue(User.class);
//
//
//                    if (user.isPaidUser()) {
//                        linearLayoutIdentity.setVisibility(View.VISIBLE);
//                        if (user.isIdentityHidden()) {
//                            checkBoxIdentityShowHidden.setChecked(true);
//                            edit.putBoolean(Constants.USERIDENTITY, true);
//                            edit.apply();
//                        } else {
//                            checkBoxIdentityShowHidden.setChecked(false);
//                            edit.putBoolean(Constants.USERIDENTITY, false);
//                            edit.apply();
//                        }
//                    } else {
//                        linearLayoutIdentity.setVisibility(View.GONE);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });


            String senderId = "";
            String recieverId = "";
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                senderId = extras.getString(Constants.USERID);
                recieverId = extras.getString(Constants.RECIEVERID);
            }


            setUserIdentity(uid);

//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
//            checkBoxIdentityShowHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Log.d(TAG, "onCheckedChanged: ");
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            User user = snapshot.getValue(User.class);
//                            if (isChecked) {
//                                user.setIdentityHidden(true);
//                                edit.putBoolean(Constants.USERIDENTITY, true);
//                                databaseReference.setValue(user);
//                            } else {
//                                user.setIdentityHidden(false);
//                                edit.putBoolean(Constants.USERIDENTITY, false);
//                                databaseReference.setValue(user);
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            });


//            String senderId = "";
//            String recieverId = "";
//            if (getIntent().getExtras() != null) {
//                Bundle extras = getIntent().getExtras();
//                senderId = extras.getString(Constants.USERID);
//                recieverId = extras.getString(Constants.RECIEVERID);
//            }


            String reciverId = senderId;


            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify = true;
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    String msg = edtMsg.getText().toString();
                    edtMsg.setText("");
                    sendMessage(msg, reciverId);


                }
            });


            DatabaseReference msg = chatDatabaseReference.child(recieverId).child(recieverId + "_" + senderId).child("Msg");
            msg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    txtMsg = findViewById(R.id.txt_msg);
                    if (snapshot.getValue() == null) {
                        txtMsg.setText("");
                    } else {
                        Message message = snapshot.getValue(Message.class);
                        Log.d(TAG, "onDataChange: MESSAGE" + message.getMesssage());
                        txtMsg.setText(message.getMesssage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
////////////////////////////////////    NOT FROM NOTIFICATION    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);
            boolean userIdentity = sharedPreferences.getBoolean(Constants.USERIDENTITY, false);


            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);
            String userStatus = sharedPreferences.getString(Constants.USERSTATUS, "");

            checkUserStatus(userStatus);


            btnUpgradeToPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upgradeToPremium();
                }
            });

            String userName = getIntent().getStringExtra(Constants.USERNAME);
            String reciverId = getIntent().getStringExtra(Constants.UID);
            String reciverIdentity = getIntent().getStringExtra(Constants.RECIEVERIDENTITYHIDDEN);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));

            setUserNameOnToolbar(userStatus, reciverIdentity, toolbar, userName);


            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify = true;
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    String msg = edtMsg.getText().toString();
                    edtMsg.setText("");
                    sendMessage(msg, reciverId);


                }
            });


            hideAndShowLayoutIdentity(uid);
            setUserIdentity(uid);
            getMsgFromFirebase(chatDatabaseReference, firebaseAuth, reciverId, txtMsg);


        }


    }

    private void getMsgFromFirebase(DatabaseReference chatDatabaseReference, FirebaseAuth firebaseAuth, String reciverId, TextView txtMsg) {


        DatabaseReference msg = chatDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).child(firebaseAuth.getCurrentUser().getUid() + "_" + reciverId).child("Msg");
        msg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    txtMsg.setText("");
                } else {

                    Message message = snapshot.getValue(Message.class);
                    txtMsg.setText(message.getMesssage());

                    DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child(reciverId);
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user.isIdentityHidden()) {
                                toolbar.setTitle("Anonymous");
                            } else {
                                toolbar.setTitle(user.getUserName());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setUserIdentity(String uid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        checkBoxIdentityShowHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);
                        if (isChecked) {
                            user.setIdentityHidden(true);
                            edit.putBoolean(Constants.USERIDENTITY, true);


                            databaseReference.setValue(user);
                        } else {
                            user.setIdentityHidden(false);
                            edit.putBoolean(Constants.USERIDENTITY, false);


                            databaseReference.setValue(user);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void hideAndShowLayoutIdentity(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setUserNameOnToolbar(String userStatus, String reciverIdentity, Toolbar toolbar, String s) {
        if (userStatus.equals("true")) {
            if (reciverIdentity.equals("true")) {
                toolbar.setTitle("Anonymous");
            } else {
                toolbar.setTitle(s);

            }
        } else {
            toolbar.setTitle(s);

        }
    }

    private void checkUserStatus(String userStatus) {
        if (userStatus.equals("true")) {
            linPremium.setVisibility(View.GONE);
        } else {
            linPremium.setVisibility(View.VISIBLE);
        }
    }

    private void upgradeToPremium() {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                user.setPaidUser(true);
                edit.putString(Constants.USERSTATUS, String.valueOf(user.isPaidUser()));
                users.setValue(user);
                linPremium.setVisibility(View.GONE);

                Snackbar.make(relativeLayoutMsg, "Upgraded to premium", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendMessage(String msg, String reciverId) {
        Message message = new Message(msg);
        chatDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).child(firebaseAuth.getCurrentUser().getUid() + "_" + reciverId).child("Msg").setValue(message);
        Log.d(TAG, "sendMessage:1------------- " + firebaseAuth.getCurrentUser().getUid() + "." + firebaseAuth.getCurrentUser().getUid() + "_" + reciverId);
        chatDatabaseReference.child(reciverId).child(reciverId + "_" + firebaseAuth.getCurrentUser().getUid()).child("Msg").setValue(message);
        Log.d(TAG, "sendMessage:2------------- " + reciverId + "." + reciverId + "_" + firebaseAuth.getCurrentUser().getUid());

        final String mesage = msg;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (notify) {
                    sendNotification(reciverId, user.getUserName(), mesage);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotification(String reciverId, String userName, String mesage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciverId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Token token = dataSnapshot.getValue(Token.class);
                    Data data;
                    boolean userIdentity = sharedPreferences.getBoolean(Constants.USERIDENTITY, false);
                    if (userIdentity) {
                        data = new Data(firebaseAuth.getCurrentUser().getUid(), R.drawable.ic_baseline_notifications_24, "Anonymous" + ": " + mesage, "New Message", reciverId);
                    } else {
                        data = new Data(firebaseAuth.getCurrentUser().getUid(), R.drawable.ic_baseline_notifications_24, userName + ": " + mesage, "New Message", reciverId);

                    }
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    Log.d(TAG, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(MessageActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.d(TAG, "onResponse: ");
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ");
                                    Toast.makeText(MessageActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");

        Token token1 = new Token(token);
        reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(token1);


    }

}