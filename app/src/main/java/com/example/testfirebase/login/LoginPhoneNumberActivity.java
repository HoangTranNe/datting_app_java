package com.example.testfirebase.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    EditText inpPhone;
    Button btnContinue;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    String verificationID;
    boolean isOtpRequestInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);


        inpPhone = findViewById(R.id.inputPhone);
        btnContinue = findViewById(R.id.btnContinue);

        String[] countryCodes = getResources().getStringArray(R.array.country_codes);// Lấy danh sách các đầu số điện thoại quốc gia từ tệp countries.xml
        Spinner spinnerCountryCodes = findViewById(R.id.spinnerGroupFourteen);// Tìm Spinner trong giao diện người dùng
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryCodes);// Tạo ArrayAdapter để hiển thị danh sách các đầu số điện thoại trong Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Định dạng giao diện cho danh sách các đầu số điện thoại (mục được hiển thị khi nhấn vào Spinner)
        spinnerCountryCodes.setAdapter(adapter);// Gán ArrayAdapter vào Spinner

        // Xử lý sự kiện khi người dùng chọn một mục trong Spinner
        spinnerCountryCodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy mục được chọn từ Spinner
                String selectedCountryCode = (String) parent.getItemAtPosition(position);
                // Xử lý các hành động sau khi người dùng chọn một đầu số điện thoại
                // Ví dụ: Hiển thị thông báo, lưu trữ giá trị, v.v.
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Toast.makeText(LoginPhoneNumberActivity.this, "Send Verification Complete", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = inpPhone.getText().toString().trim();
                String selectedCountryCode = (String) spinnerCountryCodes.getSelectedItem();
                if (isOtpRequestInProgress) {
                    Toast.makeText(LoginPhoneNumberActivity.this, "Please wait. An OTP request is already in progress.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(selectedCountryCode)) {
                    Toast.makeText(LoginPhoneNumberActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    String fullPhoneNumber = selectedCountryCode + phone;
                    // Gửi yêu cầu OTP chỉ khi không có yêu cầu nào đang được xử lý
                    sendOtpToPhoneNumber(fullPhoneNumber);
                }
            }
        });
    }
    private void sendOtpToPhoneNumber(String phoneNumber) {
        isOtpRequestInProgress = true;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60, // Timeout duration
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        isOtpRequestInProgress = false;
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // Verification failed.
                        // Display a message to the user and provide an option to retry.
                        Toast.makeText(LoginPhoneNumberActivity.this, "Verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("LoginPhoneNumActivity", "onVerificationFailed: ", e);
                        isOtpRequestInProgress = false;
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // The SMS verification code has been sent to the provided phone number.
                        // Save the verification ID and the resending token for later use.
                        verificationID = verificationId;
                        // Proceed to the next step: entering the OTP
                        proceedToInputOTPActivity();
                    }
                });
    }


    private void proceedToInputOTPActivity() {
        final String phone = inpPhone.getText().toString();
        Intent intent = new Intent(LoginPhoneNumberActivity.this, InputOTPActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("verification_id", verificationID);
        startActivity(intent);
        finish();
    }
}