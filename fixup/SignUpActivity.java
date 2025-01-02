package com.example.fixup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.fixup.utils.AndroidUtil;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    public static final String USERNAME_REGEX = "^[A-Za-z\\s]{3,30}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String CONTACT_NO_REGEX = "^[6-9]\\d{9}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
    private EditText nameText, emailText, contactNoText, stateText, cityText, passwordText, reEnterPasswordText;
    private Button registerButton;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        nameText = findViewById(R.id.userName);
        emailText = findViewById(R.id.userEmail);
        contactNoText = findViewById(R.id.userContactNo);
        stateText = findViewById(R.id.userState);
        cityText = findViewById(R.id.userCity);
        passwordText = findViewById(R.id.userPassword);
        reEnterPasswordText = findViewById(R.id.userRePassword);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.signUp_progress_bar);
        registerButton.setOnClickListener(v -> {
            AndroidUtil.setButtonEnabled(
                    registerButton,
                    false
            );
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            if(isValid()) {
                AndroidUtil.setProgressBarVisible(progressBar, true);
                String name = nameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String contactNo = contactNoText.getText().toString().trim();
                String state = stateText.getText().toString().trim();
                String city = cityText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("contactNo", contactNo);
                    map.put("state", state);
                    map.put("city", city);
                    map.put("password", password);
                    AndroidUtil.showToast(
                            this,
                            "Redirecting to OTP Verification"
                    );
                    AndroidUtil.switchActivityWithExtras(
                            this,
                            OtpVerificationActivity.class,
                            map
                    );
                }
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
            } else {
                AndroidUtil.showToast(
                        this,
                        "Enter All Details!"
                );
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.setButtonEnabled(
                        registerButton,
                        true
                );
            }
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    false
            );
            AndroidUtil.setButtonEnabled(
                    registerButton,
                    true
            );
        });
    }
    private boolean isValid() {
        if(nameText.getText().toString().trim().isEmpty()) {
            nameText.setError("Name is required");
            return false;
        }
        if(!nameText.getText().toString().trim().matches(USERNAME_REGEX)) {
            nameText.setText("");
            nameText.setError("Should contain only alphabets");
            return false;
        }
        if(emailText.getText().toString().trim().isEmpty()) {
            emailText.setText("");
            emailText.setError("Email is required");
            return false;
        }
        if(!emailText.getText().toString().trim().matches(EMAIL_REGEX)) {
            emailText.setText("");
            emailText.setError("Invalid email");
            return false;
        }
        if(contactNoText.getText().toString().trim().isEmpty()) {
            contactNoText.setText("");
            contactNoText.setError("Contact No is required");
            return false;
        }
        if(!contactNoText.getText().toString().trim().matches(CONTACT_NO_REGEX)) {
            contactNoText.setText("");
            contactNoText.setHint("Should contain only Numbers");
            return false;
        }
        if(stateText.getText().toString().trim().isEmpty()) {
            stateText.setText("");
            stateText.setError("State is required");
            return false;
        }
        if(!stateText.getText().toString().trim().matches(USERNAME_REGEX)) {
            stateText.setText("");
            stateText.setError("Should contain only alphabets");
            return false;
        }
        if(cityText.getText().toString().trim().isEmpty()) {
            cityText.setText("");
            cityText.setHint("City is required");
            return false;
        }
        if(!cityText.getText().toString().trim().matches(USERNAME_REGEX)) {
            cityText.setText("");
            cityText.setError("Should contain only alphabets");
            return false;
        }
        if(passwordText.getText().toString().trim().length() < 8) {
            passwordText.setError("Password must have at least 8 characters");
            return false;
        }
        if(!passwordText.getText().toString().trim().matches(PASSWORD_REGEX)) {
            passwordText.setText("");
            passwordText.setError("Password is weak");
            return false;
        }
        if(reEnterPasswordText.getText().toString().trim().isEmpty()) {
            reEnterPasswordText.setText("");
            reEnterPasswordText.setError("Re-Enter the password");
            return false;
        }
        if(!reEnterPasswordText.getText().toString().trim().equals(passwordText.getText().toString().trim())) {
            reEnterPasswordText.setText("");
            reEnterPasswordText.setError("Password is not matched");
            return false;
        }
        return true;
    }
}