package com.vvconnect.android.vvhereapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vvconnect.android.vvhereapp.InitialUserProfileActivity;
import com.vvconnect.android.vvhereapp.R;
import com.vvconnect.android.vvhereapp.data.UserPreferences;

public class SmsCodeLoginActivity extends AppCompatActivity implements Button.OnClickListener {

    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    private static final String USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    private static final String TAG = SmsCodeLoginActivity.class.getSimpleName();

    EditText mEditTextOtp;
    TextView mTextViewEnteredPhoneNumber;
    Button mBtnResendOtp;
    Button mBtnEditPhoneNumber;
    Button mBtnContinue;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code_login);

        mBtnEditPhoneNumber = (Button) findViewById(R.id.btn_edit_phone_number);
        mBtnResendOtp = (Button) findViewById(R.id.btn_resend_otp);
        mBtnContinue = (Button) findViewById(R.id.btn_continue);
        mBtnContinue.setOnClickListener(this);
        mBtnEditPhoneNumber.setOnClickListener(this);
        mBtnResendOtp.setOnClickListener(this);

        mEditTextOtp = (EditText) findViewById(R.id.et_otp);
        mEditTextOtp.setSelection(0);
        mTextViewEnteredPhoneNumber = (TextView) findViewById(R.id.tv_entered_phone_number);

        // Get the phone number from intent to be shown in textview.
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(USER_PHONE_NUMBER)) {
            phoneNumber = intentThatStartedThisActivity.getStringExtra(USER_PHONE_NUMBER);
            mTextViewEnteredPhoneNumber.setText(phoneNumber);
        } else {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_edit_phone_number:
                break;
            case R.id.btn_resend_otp:
                break;
            case R.id.btn_continue:
                String otp = mEditTextOtp.getText().toString();
                // TODO implement SMS authentication functionality and modify if condition.
                if (otp.length() > 0) {
                    Intent intent = new Intent(SmsCodeLoginActivity.this, InitialUserProfileActivity.class);
                    // store user phone number in shared preferences
                    UserPreferences.setUserPhoneNumber(getApplicationContext(), phoneNumber);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
}
