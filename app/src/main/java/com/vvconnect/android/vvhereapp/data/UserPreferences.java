package com.vvconnect.android.vvhereapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.vvconnect.android.vvhereapp.model.UserProfile;
import com.vvconnect.android.vvhereapp.util.TextUtil;

import java.security.InvalidParameterException;

/**
 * Created by Shyam on 10/10/17.
 */

public final class UserPreferences {

    public static final String TAG = UserPreferences.class.getSimpleName();
    public static final String PREF_USER_FIRST_NAME = "user_first_name";
    public static final String PREF_USER_LAST_NAME = "user_last_name";
    public static final String PREF_USER_PROFILE_IMAGE_PATH = "user_profile_image_path";
    public static final String PREF_USER_PHONE_NUMBER = "user_phone_number";

    /**
     * Helper method to handle setting user profile details in Preferences (first name, last name,
     * profile picture)
     *
     * @param context Context used to get the SharedPreferences
     * @param phoneNumber authenticated phone number of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param profileImagePath (currently directoy)image path of prifile image. Null can be used if no change.
     */
    public static void setUserProfile(Context context, String phoneNumber, String firstName, String lastName, @Nullable String profileImagePath) {

        if(TextUtils.isEmpty(firstName)) {
            throw new InvalidParameterException("First Name cannot be empty");
        } else if(TextUtils.isEmpty(lastName)) {
            throw new InvalidParameterException("Last Name cannot be empty");
        } else if(TextUtils.isEmpty(phoneNumber)) {
            throw new InvalidParameterException("Phone Name cannot be empty");
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_USER_FIRST_NAME, firstName);
        editor.putString(PREF_USER_LAST_NAME, lastName);
        editor.putString(PREF_USER_PHONE_NUMBER, phoneNumber);
        if(!TextUtils.isEmpty(profileImagePath)) {
            editor.putString(PREF_USER_PROFILE_IMAGE_PATH, profileImagePath);
        }
        editor.apply();
    }

    /**
     * Set the user phone number. Purpose is to store phone number when number is authenticated.
     *
     * @param context  Context used to get the SharedPreferences
     * @param phoneNumber not empty phone number string
     */
    public static void setUserPhoneNumber(Context context, String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)) {
            throw new InvalidParameterException("Phone Name cannot be empty");
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_USER_PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    /**
     * get users phone
     * @param context  Context used to get the SharedPreferences
     * @return Nullable user phone number.
     */
    public static @Nullable String getUserPhoneNumber(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumber = sp.getString(PREF_USER_PHONE_NUMBER, null);
        return phoneNumber;
    }

    /**
     * Helper method to get user profile details from shared preferences.
     *
     * @param context
     * @return UserProfile object or null if Shared Preferences are not set.
     */
    public static @Nullable UserProfile getUserProfile (Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String phoneNumber = sp.getString(PREF_USER_PHONE_NUMBER, null);
        String firstName = sp.getString(PREF_USER_FIRST_NAME, null);
        String lastName = sp.getString(PREF_USER_LAST_NAME, null);
        String profilePicturePath = sp.getString(PREF_USER_PROFILE_IMAGE_PATH, null);
        if(TextUtils.isEmpty(phoneNumber)|| TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            return null;
        }
        UserProfile userProfile = new UserProfile(firstName, lastName, phoneNumber, profilePicturePath);
        return userProfile;
    }

    /**
     * Resets the User profile stores in SharedPreferences.
     *
     * @param context Context used to get the SharedPreferences
     */
    public static void clearUserProfile(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_USER_FIRST_NAME);
        editor.remove(PREF_USER_LAST_NAME);
        editor.remove(PREF_USER_PROFILE_IMAGE_PATH);
        editor.apply();
    }

    /**
     * Checks if a user information is there in shared preferences
     *
     * @param context Context used to get the SharedPreferences
     * @return if the user profile infomation is not empty
     */
    public static boolean isUserProfileStored(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String firstName = sp.getString(PREF_USER_FIRST_NAME, null);
        boolean isUserProfileStored = !TextUtils.isEmpty(firstName);
        return isUserProfileStored;
    }

}
