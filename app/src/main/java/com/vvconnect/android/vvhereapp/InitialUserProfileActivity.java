package com.vvconnect.android.vvhereapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vvconnect.android.vvhereapp.data.UserPreferences;
import com.vvconnect.android.vvhereapp.util.ApplicationUtils;
import com.vvconnect.android.vvhereapp.util.BitmapUtils;

import java.io.FileNotFoundException;


public class InitialUserProfileActivity extends AppCompatActivity implements Button.OnClickListener{

    private static final String TAG = InitialUserProfileActivity.class.getSimpleName();
    private static final int RESULT_LOAD_IMAGE = 37, REQUEST_CAMERA = 41;
    private static final String TAKE_PHOTO = "Take photo", CANCEL = "cancel", CHOOSE_FROM_LIBRARY = "Choose from library";
    private String userChoosenTask;
    Button mBtnFinish;
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    ImageButton mImageBtnProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_user_profile);

        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mEditTextFirstName = (EditText) findViewById(R.id.et_first_name);
        mEditTextLastName = (EditText) findViewById(R.id.et_last_name);
        mImageBtnProfileImage = (ImageButton) findViewById(R.id.ib_profile_image);

        mBtnFinish.setOnClickListener(this);
        mImageBtnProfileImage.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_finish:
                if (!performEditTextValidations()) return;
                storeUserProfile();
                // start the home or main activity with extras if required.
                Intent intentToStartMainActivity = new Intent(InitialUserProfileActivity.this,
                        MainActivity.class);
                // TODO create a new login flag in main activity via intent extra to perform functions accordingly.
                startActivity(intentToStartMainActivity);
                finish();
                break;
            case R.id.ib_profile_image:
                selectImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ApplicationUtils.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(TAKE_PHOTO))
                        photoFromCameraIntent();
                    else if(userChoosenTask.equals(CHOOSE_FROM_LIBRARY))
                        photoFromLibraryIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "File permission denied",Toast.LENGTH_SHORT);
                }
                break;
            case ApplicationUtils.PERMISSIONS_REQUEST_ACCESS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(TAKE_PHOTO))
                        photoFromCameraIntent();
                    else if(userChoosenTask.equals(CHOOSE_FROM_LIBRARY))
                        photoFromLibraryIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "Camera permission denied",Toast.LENGTH_SHORT);
                }
        }

    }

    /**
     * stores the user data in shared preferences.
     * Assumes that the field entries are validated
     */
    private void storeUserProfile() {
        String phoneNumber = UserPreferences.getUserPhoneNumber(getApplicationContext());
        String firstName = mEditTextFirstName.getText().toString();
        String lastName = mEditTextLastName.getText().toString();
        //TODO implement profile image update functionality
        if(TextUtils.isEmpty(phoneNumber)) {
            throw new IllegalStateException("Phone number has be set first in the shared preferences");
        }
        String profileImagePath = null;
        UserPreferences.setUserProfile(getApplicationContext(),
                phoneNumber,
                firstName,
                lastName,
                profileImagePath);
    }

    /**
     * Performs validation check on edit text fields and sets error in the layout
     *
     * @return if validation check where successful
     */
    private boolean performEditTextValidations() {
        if(mEditTextFirstName.getText().toString().length() == 0) {
            mEditTextFirstName.setError("First name is required!");
            return false;
        }
        else if (mEditTextLastName.getText().toString().length() == 0) {
            mEditTextLastName.setError("Last name is required");
            return false;
        }
        return true;
    }


    private void selectImage() {
        final CharSequence[] items = { CHOOSE_FROM_LIBRARY, TAKE_PHOTO,
                CANCEL };

        AlertDialog.Builder builder = new AlertDialog.Builder(InitialUserProfileActivity.this);
        builder.setTitle("Add profile picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean filePermission = ApplicationUtils.checkFilePermission(InitialUserProfileActivity.this);
                boolean cameraPermission = ApplicationUtils.checkCameraPermission(InitialUserProfileActivity.this);
                if (item == 0) {
                    userChoosenTask = CHOOSE_FROM_LIBRARY;
                    if(filePermission)
                        photoFromLibraryIntent();

                }
                else if (item == 1) {
                    userChoosenTask = TAKE_PHOTO;
                    if(cameraPermission)
                        photoFromCameraIntent();

                } else if (item == 2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void photoFromLibraryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), RESULT_LOAD_IMAGE);
    }

    private void photoFromCameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult Permission +" + requestCode+ " "+resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                onSelectImageFromLibrary(data);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageFromCamera(data);
            }
        }
    }

    private void onSelectImageFromLibrary(Intent data) {
        if (data != null) {
            Uri contentURI = data.getData();
            try {
                Bitmap imageBitmap = BitmapUtils.decodeUri(getApplicationContext(), contentURI);
                mImageBtnProfileImage.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageFromCamera(Intent data) {
        // TODO implement this functionality
        Toast.makeText(getApplicationContext(), "Functionality yet to be implemented" , Toast.LENGTH_SHORT).show();
    }
}
