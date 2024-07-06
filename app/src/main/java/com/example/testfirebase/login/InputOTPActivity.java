package com.example.testfirebase.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.checkerframework.checker.nullness.qual.NonNull;

public class InputOTPActivity extends AppCompatActivity {

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0){
                if(selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyBoard(otp2);
                }else if(selectedETPosition == 1){
                    selectedETPosition = 2;
                    showKeyBoard(otp3);
                }else if(selectedETPosition == 2){
                    selectedETPosition = 3;
                    showKeyBoard(otp4);
                }
                else if(selectedETPosition == 3){
                    selectedETPosition = 4;
                    showKeyBoard(otp5);
                }
                else if(selectedETPosition == 4){
                    selectedETPosition = 5;
                    showKeyBoard(otp6);
                }
            }
        }
    };
    EditText otp1,otp2,otp3,otp4,otp5,otp6;
    TextView resendOTP;
    Boolean resendEnable = false;
    Integer resendTime = 60;

    Integer selectedETPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otpactivity);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        resendOTP = findViewById(R.id.resendBTN);

        final Button verifyBTN = findViewById(R.id.verifyBTN);
        final TextView inpPhone = findViewById(R.id.inputPhone);

        final String getMobile = getIntent().getStringExtra("inphone");

        inpPhone.setText(getMobile);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);

        showKeyBoard(otp1);

        startCountDownTimer();
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnable){
                    startCountDownTimer();
                }
            }
        });

        verifyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String generateOTP =
                        otp1.getText().toString()+
                                otp2.getText().toString()+
                                otp3.getText().toString()+
                                otp4.getText().toString()+
                                otp5.getText().toString()+
                                otp6.getText().toString();
                if (generateOTP.length() == 6) {
                    // Verify the OTP with Firebase
                    String verificationId = getIntent().getStringExtra("verification_id");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, generateOTP);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(InputOTPActivity.this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // You can handle the successful sign-in here, for example:
                            Toast.makeText(InputOTPActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
                            // Proceed to the next activity or perform any desired actions.

                            // Example: Proceed to a home activity after successful sign-in
                            Intent intent = new Intent(InputOTPActivity.this, InputNameActivity.class);
                            startActivity(intent);
                            finish(); // Finish the current activity to prevent the user from going back to the OTP screen.
                        } else {
                            // Sign in failed, display a message to the user and provide an option to retry.
                            Toast.makeText(InputOTPActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.w("InputOTPActivity", "signInWithCredential:failure", task.getException());
                            // If you want to retry, you can provide an option here to resend the OTP.
                        }
                    }
                });
    }

    private void  showKeyBoard(EditText otpET){
        otpET.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }
    private void startCountDownTimer(){
        resendEnable = false;
        resendOTP.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resendOTP.setText("Resend Code (" + (millisUntilFinished/1000)+")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                resendOTP.setText("Resend Code");
                resendOTP.setTextColor(getResources().getColor(R.color.blue_300));
            }
        }.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(selectedETPosition == 5){
                selectedETPosition = 4;
                showKeyBoard(otp5);
            }
            else if(selectedETPosition == 4){
                selectedETPosition = 3;
                showKeyBoard(otp4);
            }
            else if(selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyBoard(otp3);
            }else if(selectedETPosition ==2){
                selectedETPosition=1;
                showKeyBoard(otp2);
            }
            else if(selectedETPosition == 1){
                selectedETPosition = 0;
                showKeyBoard(otp1);
            }

            return true;
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
    }
}