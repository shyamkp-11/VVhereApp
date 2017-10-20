package com.vvconnect.android.vvhereapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vvconnect.android.vvhereapp.data.UserPreferences;
import com.vvconnect.android.vvhereapp.login.RegisterActivity;
import com.vvconnect.android.vvhereapp.model.UserProfile;
import com.vvconnect.android.vvhereapp.util.ApplicationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Class is temporary just to get started with map and navigation drawer
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static DrawerLayout mDrawerLayout;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static Toolbar toolbar;
    private static FragmentManager fragmentManager;
    private static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(Timber.treeCount() == 0) {
            // Set up Timber logging library
            Timber.plant(new Timber.DebugTree());
        }

        // if user is not logged in, we start with signup
        if(!ApplicationUtils.isUserLoggedIn(getApplicationContext())) {
            Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerActivityIntent);
            finish();
            return;
        }

        UserProfile userProfile = UserPreferences.getUserProfile(getApplicationContext());
        Timber.d("User profile picture is stored at"+userProfile.getProfileImagePath());



        getSupportActionBar().hide();
        initViews();
        setUpHeaderView();
        setupDrawerContent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Debug
        UserPreferences.clearUserProfile(getApplicationContext());
    }


    /*  Init all views  */
    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.slider_menu);
        fragmentManager = getSupportFragmentManager();
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, // nav menu toggle icon
                R.string.drawer_open, // nav drawer open - description for
                // accessibility
                R.string.drawer_close // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * For using header view use this method
     **/
    private void setUpHeaderView() {
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header_view);
        TextView displayName = headerView.findViewById(R.id.username);
        CircleImageView profilePic = headerView.findViewById(R.id.iv_profile_pic);

        // Setup name and profile pic in navigation drawer
        UserProfile userProfile = UserPreferences.getUserProfile(getApplicationContext());
        displayName.setText(userProfile.getFirstName());
        String path = userProfile.getProfileImagePath();
        if(!TextUtils.isEmpty(path)) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(path)));
                profilePic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /*  Method for Navigation View item selection  */
    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {


        Toast.makeText(MainActivity.this, "You Clicked on \"" + menuItem.getTitle().toString() + "\" menu item.", Toast.LENGTH_SHORT).show();
        switch (menuItem
                .getItemId()) {
            case R.id.home:
                //Replace fragment
                break;

            case R.id.notifications:
                break;

            case R.id.share_app:
                //Start new Activity or do your stuff
                break;

            case R.id.settings:
                break;

            case R.id.help:
                break;
        }
        //Closing drawer on item click
        mDrawerLayout.closeDrawers();

        //Check and un-check menu item if they are checkable behaviour
        if (menuItem.isCheckable()) {
            if (menuItem.isChecked()) menuItem.setChecked(false);
            else menuItem.setChecked(true);
        }
    }

    //On back press check if drawer is open and closed
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(MainActivity.this, "MAP ready", Toast.LENGTH_SHORT).show();
        LatLng chicago = new LatLng(41.878, -87.62);
        googleMap.addMarker(new MarkerOptions().position(chicago)
                .title("Marker in Chicago"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(chicago));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
    }
}