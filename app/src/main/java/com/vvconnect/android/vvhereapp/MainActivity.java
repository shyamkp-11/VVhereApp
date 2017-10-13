package com.vvconnect.android.vvhereapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.vvconnect.android.vvhereapp.data.UserPreferences;
import com.vvconnect.android.vvhereapp.login.RegisterActivity;
import com.vvconnect.android.vvhereapp.model.UserProfile;
import com.vvconnect.android.vvhereapp.util.ApplicationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
            return;
        }

        UserProfile userProfile = UserPreferences.getUserProfile(getApplicationContext());
        Log.e("MAIN ACTIVITY", userProfile.getProfileImagePath());
        loadImageFromStorage(userProfile.getProfileImagePath());
        UserPreferences.clearUserProfile(getApplicationContext());

    }
    private void loadImageFromStorage(String path)
    {

        try {
            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}