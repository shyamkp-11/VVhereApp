package com.vvconnect.android.vvhereapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vvconnect.android.vvhereapp.login.RegisterActivity;
import com.vvconnect.android.vvhereapp.util.ApplicationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if user is not logged in, we start with signup
        if(!ApplicationUtils.isUserLoggedIn(getApplicationContext())) {
            Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerActivityIntent);
            finish();
        }
    }
}
