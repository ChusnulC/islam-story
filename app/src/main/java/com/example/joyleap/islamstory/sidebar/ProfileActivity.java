package com.example.joyleap.islamstory.sidebar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.joyleap.islamstory.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
