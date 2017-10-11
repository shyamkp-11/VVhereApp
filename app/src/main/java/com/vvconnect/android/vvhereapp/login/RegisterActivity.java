package com.vvconnect.android.vvhereapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.vvconnect.android.vvhereapp.R;

/**
 *
 * Activity class to registere user or if alredy registered then log in
 */
public class RegisterActivity extends AppCompatActivity implements Button.OnClickListener{

    public final String TAG = RegisterActivity.class.getSimpleName();
    EditText mEditTextPhoneNumber;
    Button mBtnRegister;
    CountryCodePicker mCountryCodePicker;


    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mBtnRegister = (Button) findViewById(R.id.btn_register_next);
        mBtnRegister.setOnClickListener(this);

        mEditTextPhoneNumber = (EditText) findViewById(R.id.et_phone_register);
//        mEditTextPhoneNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Make next button visible
//                if(s.toString().length() == getResources().getInteger(R.integer.phone_number_digits)) {
//                    mBtnRegister.setVisibility(View.VISIBLE);
//                }else{
//                    mBtnRegister.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        mCountryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        mCountryCodePicker.registerCarrierNumberEditText(mEditTextPhoneNumber);
        mCountryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code
                if(isValidNumber) {
                    mBtnRegister.setEnabled(true);
                } else {
                    mBtnRegister.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_register_next) {
            Intent intentToStartLoginActivity = new Intent(RegisterActivity.this, SmsCodeLoginActivity.class);
            String formattedFullNumber = mCountryCodePicker.getFormattedFullNumber();
            intentToStartLoginActivity. putExtra(SmsCodeLoginActivity.USER_PHONE_NUMBER, formattedFullNumber);
            startActivity(intentToStartLoginActivity);
        }
    }
}
