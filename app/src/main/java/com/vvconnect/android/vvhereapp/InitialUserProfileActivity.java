package com.vvconnect.android.vvhereapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vvconnect.android.vvhereapp.data.UserPreferences;
import com.vvconnect.android.vvhereapp.util.ApplicationUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class InitialUserProfileActivity extends AppCompatActivity implements Button.OnClickListener{

    public static final String PROFILE_PIC_FILE_NAME = "profile_pic.jpg";
    private static final String IMAGE_DIR_NAME = "imageDir";
    String mProfilePicPath = null;
    Button mBtnFinish;
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    TextView mTextViewEditProfilePic;
    TextView mTextViewUploadEditProfilePic;
    CircleImageView mImageViewProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_user_profile);

        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mTextViewEditProfilePic = (TextView) findViewById(R.id.tv_profile_pic_edit);
        mTextViewUploadEditProfilePic = (TextView) findViewById(R.id.tv_upload_profile_pic_text);
        mEditTextFirstName = (EditText) findViewById(R.id.et_first_name);
        mEditTextLastName = (EditText) findViewById(R.id.et_last_name);
        mImageViewProfilePic = (CircleImageView) findViewById(R.id.iv_profile_pic);

        mBtnFinish.setOnClickListener(this);
        mImageViewProfilePic.setOnClickListener(this);

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
            case R.id.iv_profile_pic:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                        .setFixAspectRatio(true)
                        .setAspectRatio(1, 1)
                        .setOutputUri(getProfilePicFileUri(IMAGE_DIR_NAME, PROFILE_PIC_FILE_NAME))
                        .start(this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ApplicationUtils.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                break;
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

        if(TextUtils.isEmpty(phoneNumber)) {
            throw new IllegalStateException("Phone number has be set first in the shared preferences");
        }
        UserPreferences.setUserProfile(getApplicationContext(),
                phoneNumber,
                firstName,
                lastName,
                mProfilePicPath);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("onActivityResult Permission +" + requestCode+ " "+resultCode);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageViewProfilePic.setImageURI(resultUri);
                Toast.makeText(getApplicationContext(),resultUri.getPath(),Toast.LENGTH_LONG).show();
                mProfilePicPath = resultUri.getPath();
                profilePictureSelected();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * hide upload message. and optionally show edit image textview.
     */
    private void profilePictureSelected() {
        mTextViewEditProfilePic.setVisibility(View.VISIBLE);
        mTextViewUploadEditProfilePic.setVisibility(View.GONE);
    }


    /**
     *
     * @return the uri of the file to save the image
     */
    private Uri getProfilePicFileUri(String dirName, String fileName) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/VVhereApp/app_data/imageDir
        File directory = cw.getDir(dirName, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, fileName);

        return Uri.fromFile(mypath);
    }
}
