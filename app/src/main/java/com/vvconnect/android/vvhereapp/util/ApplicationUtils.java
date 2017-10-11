package com.vvconnect.android.vvhereapp.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.vvconnect.android.vvhereapp.data.UserPreferences;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by Shyam on 10/10/17.
 * Utility class to abstract Application level functions.
 */
public class ApplicationUtils {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 112;
    public static final int PERMISSIONS_REQUEST_ACCESS_CAMERA = 110;

    public static boolean isUserLoggedIn(Context context) {
        if(UserPreferences.isUserProfileStored(context)) return true;
        else return false;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkFilePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    // TODO make string resources for alerts strings
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        //TODO verify the target api
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                else {
                    // ask for permission directly
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }

                return false;
            } else {
                // permission are already granted
                return true;
            }
        } else {
            // for lower android version return true as permissions are already asked in manifest.
            return true;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCameraPermission(final Context context) {
        String[] permissionArray = {Manifest.permission.CAMERA};
        int requestCode = PERMISSIONS_REQUEST_ACCESS_CAMERA;
        String alertTitle = "Permission required";
        String alertMessage = "Camera permission required to access Camera";
        return genericPermissionChecker(context, permissionArray, requestCode, alertTitle, alertMessage);
    }

    private static boolean genericPermissionChecker(final Context context, final String [] permissionArray, final int requestCode, String alertTtile, String alertMessage) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(context, permissionArray[0])
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionArray[0])) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(alertTtile);
                    alertBuilder.setMessage(alertMessage);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, permissionArray, requestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
                else {
                    // ask for permission directly
                    ActivityCompat.requestPermissions((Activity) context, permissionArray, requestCode);
                }

                return false;
            } else {
                // permission are already granted
                return true;
            }
        } else {
            // for lower android version return true as permissions are already asked in manifest.
            return true;
        }
    }
}
