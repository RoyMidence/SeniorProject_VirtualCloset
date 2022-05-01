package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
private DrawerLayout drawer;
private DatabaseHelper mDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(getApplicationContext());



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if(savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AddClothesFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_add);}

        if (mDatabaseHelper.loggedUserTableEmpty()) {
            Intent go = new Intent(MainActivity.this,LoginActivity.class);
                   startActivity(go);
//            mDatabaseHelper.logginUser("1");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddClothesFragment()).commit();

                break;
            case R.id.nav_closet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TheClothesFragment()).commit();

                break;
            case R.id.nav_weather:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeatherFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this,"You have been logged out.",Toast.LENGTH_SHORT).show();
                mDatabaseHelper.logOut();
                Intent go = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(go);

                break;
            case R.id.nav_shareCloset:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShareCloset()).commit();
                break;

            case R.id.nav_outfit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OutfitFragment()).commit();
                break;
            case R.id.nav_randomOutfitFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RandomizeOutfit()).commit();
                break;
            case R.id.nav_userPreferences:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserPreferences()).commit();
                break;
            case R.id.nav_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void  onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }

    }
}