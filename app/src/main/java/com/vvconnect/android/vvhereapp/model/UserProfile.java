package com.vvconnect.android.vvhereapp.model;

import android.support.annotation.Nullable;

/**
 * Created by Shyam on 10/10/17.
 */

public class UserProfile {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private @Nullable String profileImagePath;

    public UserProfile(String firstName, String lastName, @Nullable String profileImagePath, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImagePath = profileImagePath;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Nullable
    public String getProfileImagePath() {
        return profileImagePath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
