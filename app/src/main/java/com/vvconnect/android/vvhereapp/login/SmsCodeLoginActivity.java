package com.vvconnect.android.vvhereapp.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.vvconnect.android.vvhereapp.InitialUserProfileActivity;
import com.vvconnect.android.vvhereapp.R;
import com.vvconnect.android.vvhereapp.data.UserPreferences;
import com.vvconnect.android.vvhereapp.util.TextUtil;

public class SmsCodeLoginActivity extends AppCompatActivity implements Button.OnClickListener {

    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    private static final String USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    private static final String TAG = SmsCodeLoginActivity.class.getSimpleName();

//    EditText mEditTextOtp;
    PinEntryEditText mPinEntryEditText;
    TextView mTextViewVerifyNumber;
    TextView mTextViewMessageWrongNumber;
    Button mBtnResendOtp;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_code_login);


        mBtnResendOtp = (Button) findViewById(R.id.btn_resend_otp);
        mBtnResendOtp.setOnClickListener(this);

//        mEditTextOtp = (EditText) findViewById(R.id.et_otp);
//        mEditTextOtp.setSelection(0);
        mPinEntryEditText = (PinEntryEditText) findViewById(R.id.pet_pin_entry);

        mTextViewVerifyNumber = (TextView) findViewById(R.id.tv_verify_number);
        mTextViewMessageWrongNumber = (TextView) findViewById(R.id.tv_message_wrong_number);

        // Get the phone number from intent to be shown in textview.
        Intent intentThatStartedThisActivity = getIntent();
        if (!intentThatStartedThisActivity.hasExtra(USER_PHONE_NUMBER)) {
            finish();
        }
        else {
            phoneNumber = intentThatStartedThisActivity.getStringExtra(USER_PHONE_NUMBER);
            // set the title string
            String outputString = getResources().getString(R.string.verify_phone, phoneNumber);
            mTextViewVerifyNumber.setText(outputString);
            // set message string
            String messageString = getResources().getString(R.string.message_wrong_number, phoneNumber);
            // make the Wrong number substring clickable..
            CharSequence styledString = TextUtil.fromHtml(messageString);
            SpannableString ss = new SpannableString(styledString);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    // go back
                    finish();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            int startIndex = styledString.toString().indexOf("Wrong number?");
            if (startIndex != -1) {
                ss.setSpan(clickableSpan, startIndex, startIndex + 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mTextViewMessageWrongNumber.setText(ss);
            mTextViewMessageWrongNumber.setMovementMethod(LinkMovementMethod.getInstance());
        }
        //
        if (mPinEntryEditText != null) {
            mPinEntryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals("1234")) {
                        startInitialProfileActivity();
                    } else {
                        Toast.makeText(SmsCodeLoginActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        mPinEntryEditText.setText(null);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_resend_otp:
                break;
        }
    }

    public void startInitialProfileActivity() {
        Intent intent = new Intent(SmsCodeLoginActivity.this, InitialUserProfileActivity.class);
        // store user phone number in shared preferences
        UserPreferences.setUserPhoneNumber(getApplicationContext(), phoneNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
