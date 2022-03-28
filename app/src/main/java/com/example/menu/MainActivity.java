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

        if (mDatabaseHelper.tagTableEmpty()) {
            mDatabaseHelper.addToTagsTable("Formal"); // 1
            mDatabaseHelper.addToTagsTable("Casual");// 2
            mDatabaseHelper.addToTagsTable("Athletic"); // 3
            mDatabaseHelper.addToTagsTable("Winter");// 4
            mDatabaseHelper.addToTagsTable("Spring");// 5
            mDatabaseHelper.addToTagsTable("Summer");// 6
            mDatabaseHelper.addToTagsTable("Fall"); // 7
            mDatabaseHelper.addToTagsTable("All"); // 8
        }

        if (mDatabaseHelper.userTableEmpty()) {
            mDatabaseHelper.addUser("Person1", "Roy", "abcd");
            mDatabaseHelper.addUser("Person2", "abcdef", "abcd");
            mDatabaseHelper.addUser("Person3", "abcdef", "abcd");
            mDatabaseHelper.addUser("Person4", "abcdef", "abcd");
        }

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