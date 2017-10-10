package com.vvconnect.android.vvhereapp.login;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vvconnect.android.vvhereapp.R;
import com.vvconnect.android.vvhereapp.util.TextUtil;

public class SmsCodeLogin extends AppCompatActivity implements Button.OnClickListener{

    private static final String USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    EditText mEditTextOtp;
    TextView mTextViewEnteredPhoneNumber;
    Button mBtnResendOtp;
    Button mBtnEditPhoneNumber;

    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code_login);

        mBtnEditPhoneNumber = (Button) findViewById(R.id.btn_edit_phone_number);
        mBtnResendOtp = (Button) findViewById(R.id.btn_resend_otp);
        mEditTextOtp = (EditText) findViewById(R.id.et_otp);
        mTextViewEnteredPhoneNumber = (TextView) findViewById(R.id.tv_entered_phone_number);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(USER_PHONE_NUMBER)) {
            String phone = intentThatStartedThisActivity.getStringExtra(USER_PHONE_NUMBER);
            mTextViewEnteredPhoneNumber.setText(phone);
        } else {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case  R.id.btn_edit_phone_number:
                break;
            case  R.id.btn_resend_otp:
                break;
        }
    }
}
