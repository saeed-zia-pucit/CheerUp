package com.app.cheerthemup.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity1 extends AppCompatActivity {

    private static final String TAG = "MessageActivity1";
    MaterialButton btnSend, btnUpgradeToPremium;
    TextInputEditText edtMsg;
    TextView txtMsg;
    Toolbar toolbar;
    LinearLayout linPremium;
    RelativeLayout relativeLayoutMsg;


    //Firebase
    FirebaseAuth firebaseAuth;

    //fcm api
    public static final String BASE_URL = "https://fcm.googleapis.com/";
    APIService apiService;
    boolean notify = false;


    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message1);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = prefs.edit();

        apiService = Client.getClient(BASE_URL).create(APIService.class);

        relativeLayoutMsg = findViewById(R.id.rel_msg);
        linPremium = findViewById(R.id.lin_premium);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        btnUpgradeToPremium = findViewById(R.id.btn_upgrade_to_premium);
        btnSend = findViewById(R.id.btn_send);
        edtMsg = findViewById(R.id.edit_msg);
        txtMsg = findViewById(R.id.txt_msg);

        txtMsg.setMovementMethod(new ScrollingMovementMethod());
        String notiExtra = getIntent().getStringExtra(Constants.fromNotification);


//        if (prefs.getString(Constants.fromNotification, "").equals("true")) {
        if (notiExtra.equals("true")) {
            //////////////////////////////////////FROM NOTIIFICATION
            Log.d(TAG, "onCreate: noti extra true");


            String userName = getIntent().getStringExtra(Constants.USERNAME);
            String recieverUid = getIntent().getStringExtra(Constants.UID);
            String userStatus = prefs.getString(Constants.USERSTATUS, "");

            String[] split = userName.split(":");
            setUserNameOnToolbar(recieverUid, split[0]);
            getMsgFromFirebase(recieverUid, txtMsg);
            checkUserStatus(userStatus);

            btnUpgradeToPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upgradeToPremium();
                }
            });
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify = true;
                    String msg = edtMsg.getText().toString();
                    edtMsg.setText("");
                    sendMessage(msg, recieverUid);


                }
            });


        } else {
            //////////////////////////////////////NOT FROM NOTIIFICATION
            Log.d(TAG, "onCreate: noti extra false");

            String userName = getIntent().getStringExtra(Constants.USERNAME);
            String recieverUid = getIntent().getStringExtra(Constants.UID);
            String userStatus = prefs.getString(Constants.USERSTATUS, "");

            setUserNameOnToolbar(recieverUid, userName);
            getMsgFromFirebase(recieverUid, txtMsg);
            checkUserStatus(userStatus);


            btnUpgradeToPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upgradeToPremium();
                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify = true;
                    String msg = edtMsg.getText().toString();
                    edtMsg.setText("");
                    sendMessage(msg, recieverUid);


                }
            });

        }


    }


    private void sendMessage(String msg, String reciverId) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference chatDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat");


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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity1.this);
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
                                            Toast.makeText(MessageActivity1.this, "failed", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.d(TAG, "onResponse: ");
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure: ");
                                    Toast.makeText(MessageActivity1.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMsgFromFirebase(String reciverId, TextView txtMsg) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference chatDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        DatabaseReference msg = chatDatabaseReference.child(firebaseAuth.getCurrentUser().getUid()).child(firebaseAuth.getCurrentUser().getUid() + "_" + reciverId).child("Msg");
        msg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    txtMsg.setText("");
                } else {

                    Message message = snapshot.getValue(Message.class);
                    txtMsg.setText(message.getMesssage());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setUserNameOnToolbar(String recieverUid, String userName) {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child(recieverUid);
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user.isIdentityHidden()) {
                    toolbar.setTitle(user.getAnonymousUser());
                } else {
                    toolbar.setTitle(userName);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void checkUserStatus(String userStatus) {
        if (userStatus.equals("true")) {
            linPremium.setVisibility(View.GONE);
        } else {
            linPremium.setVisibility(View.VISIBLE);
        }
    }
}