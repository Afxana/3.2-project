package com.example.project32;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class UserHomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        drawerLayout = findViewById(R.id.main);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.open();
            }
        });

        View headerView = navigationView.getHeaderView(0);
        ImageView useImage = headerView.findViewById(R.id.userImage);
        TextView textUsername = headerView.findViewById(R.id.textUserName);

        useImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserHomeActivity.this, textUsername.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.nav_venues){
                    Toast.makeText(UserHomeActivity.this,"Venues clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, UserHomeActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_booking){
                    Toast.makeText(UserHomeActivity.this,"Booking clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, BookingActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_wishlist){
                    Toast.makeText(UserHomeActivity.this,"Wishlist clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, WishlistActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_feedback){
                    Toast.makeText(UserHomeActivity.this,"Feedback clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_listing){
                    Toast.makeText(UserHomeActivity.this,"Listing clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, ListingActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_budget){
                    Toast.makeText(UserHomeActivity.this,"Budget tracking clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, BudgetActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.nav_planner){
                    Toast.makeText(UserHomeActivity.this,"Planner clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, PlanningActivity.class);
                    startActivity(intent);
                }

                if (itemId == R.id.nav_logout) {
                    Toast.makeText(UserHomeActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomeActivity.this, LogoutActivity.class);
                    startActivity(intent);
                }


                drawerLayout.close();

                return false;
            }
        });



    }
}