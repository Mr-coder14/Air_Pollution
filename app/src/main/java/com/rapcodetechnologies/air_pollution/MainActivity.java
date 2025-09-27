package com.rapcodetechnologies.air_pollution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }else if(item.getItemId()==R.id.nav_cities){
                selectedFragment=new fragment_cities();
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
            }
            return true;
        });
    }

}