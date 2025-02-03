package com.example.project32;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class OwnerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        firestore = FirebaseFirestore.getInstance();


        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navDashboard){
                    Toast.makeText(OwnerHomeActivity.this,"Dashboard clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerHomeActivity.this, OwnerHomeActivity.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.navNotifications){
                    Toast.makeText(OwnerHomeActivity.this, "Notifications clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerHomeActivity.this, OwnerNotification.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.navAdd){
                    Toast.makeText(OwnerHomeActivity.this, "Add venue clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerHomeActivity.this, AddVenueActivity.class);
                    startActivity(intent);
                }
                else if(itemId == R.id.navUpdate){
                    Toast.makeText(OwnerHomeActivity.this, "Update venue clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerHomeActivity.this, UpdateVenueActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(OwnerHomeActivity.this, "Delete venue clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerHomeActivity.this, DeleteVenueActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

}