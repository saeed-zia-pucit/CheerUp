package com.app.cheerthemup.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.app.cheerthemup.utils.Constants;
import com.app.cheerthemup.views.fragments.ProfileFragment;
import com.app.cheerthemup.R;
import com.app.cheerthemup.views.fragments.UsersFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    ImageButton imageButtonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        navigationView = findViewById(R.id.nav_view);


        navigationView.setItemIconTintList(null);

        drawerLayout = findViewById(R.id.drawer_layout);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
//
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();


        imageButtonMenu = findViewById(R.id.btn_menu);
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {

                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });


        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .commit();
//        navigationView.setCheckedItem(R.id.nav_profile);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, profileFragment)
                                .addToBackStack(Constants.PROFILE_FRAGMENT)
                                .commit();

//                        navigationView.setCheckedItem(R.id.nav_profile);

                        break;
                    case R.id.nav_users:
                        UsersFragment userFragment = new UsersFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, userFragment)
                                .addToBackStack(Constants.USERS_FRAGMENT)
                                .commit();

//                        navigationView.setCheckedItem(R.id.nav_users);
                        break;

                    case R.id.nav_logout:
//                        MyPortalFragment myPortalFragment = new MyPortalFragment();
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container, myPortalFragment)
//                                .commit();

                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.clear().apply();
                        startActivity(new Intent(DashboardActivity.this, SigninActivity.class));

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Log.d(TAG, "onBackPressed: 1" + getSupportFragmentManager().getBackStackEntryCount());
                getSupportFragmentManager().popBackStack();
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                Log.d(TAG, "onBackPressed: 2" + getSupportFragmentManager().getBackStackEntryCount());

                super.onBackPressed();
            }
        }
    }
}