package com.example.fixup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.apicalls.models.OTPRequestModel;
import com.example.fixup.models.UserDetailsModel;
import com.example.fixup.utils.AndroidUtil;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpVerificationActivity extends AppCompatActivity {
    private EditText userEnterdOTP;
    private String generatedOTP;
    private Button sendOTP, verifyOTP;
    private String name, email, contactNo, state, city, password;
    private String base_uri;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        base_uri = getResources().getString(R.string.base_url);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        contactNo = getIntent().getStringExtra("contactNo");
        state = getIntent().getStringExtra("state");
        city = getIntent().getStringExtra("city");
        password = getIntent().getStringExtra("password");
        Toolbar toolbar = findViewById(R.id.otp_toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.OTP_progress_bar);
        userEnterdOTP = findViewById(R.id.otp_text);
        sendOTP = findViewById(R.id.send_resend_otp_button);
        verifyOTP = findViewById(R.id.verify_otp_button);
        sendOTP.setOnClickListener(v -> {
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            AndroidUtil.setButtonEnabled(
                    sendOTP,
                    false
            );
            sendOTPToUserViaEmail();
        });
        verifyOTP.setOnClickListener(v -> {
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            AndroidUtil.setButtonEnabled(
                    verifyOTP,
                    false
            );
            verifyOTP();
        });
    }
    private void sendOTPToUserViaEmail() {
        AndroidUtil.setProgressBarVisible(
                progressBar,
                true
        );
        Retrofit retrofit = RetroFitClient.getClient(base_uri);
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        Call<OTPRequestModel> call = apiService.sendOTP(map);
        call.enqueue(new Callback<OTPRequestModel>() {
            @Override
            public void onResponse(Call<OTPRequestModel> call, Response<OTPRequestModel> response) {
                Log.e("OTP Response", "Status Code: " + response.code());
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if(response.isSuccessful() && response.body() != null) {
                    try {
                        generatedOTP = response.body().getOtp();
                        AndroidUtil.showToast(
                                getApplicationContext(),
                                "OTP sent to your registered email!"
                        );
                    } catch (Exception e) {
                        AndroidUtil.showToast(
                                getApplicationContext(),
                                "Failed to send OTP"
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPRequestModel> call, Throwable t) {
                Log.e("OTP Failure", "Error: ", t);
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed"
                );
                Log.e("OtpVerificationActivity", "Request Failed: ", t);
            }
        });
        AndroidUtil.setButtonEnabled(
                sendOTP,
                true
        );
    }
    private void verifyOTP() {
        if(generatedOTP != null && generatedOTP.equals(userEnterdOTP.getText().toString().trim())) {
            registerUser();
            AndroidUtil.showToast(
                    getApplicationContext(),
                    "OTP Verified Successfully"
            );
        } else {
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    false
            );
            AndroidUtil.showToast(
                    getApplicationContext(),
                    "OTP is invalid. Please try again"
            );
        }
        AndroidUtil.setButtonEnabled(
                verifyOTP,
                true
        );
    }

    private void registerUser() {
        Retrofit retrofit = RetroFitClient.getClient(base_uri);
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("contactNo", contactNo);
        map.put("state", state);
        map.put("city", city);
        map.put("password", password);
        Call<Void> call = apiService.register(map);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if(response.isSuccessful()) {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "User Registered successfully!"
                    );
                    AndroidUtil.switchActivity(
                            getApplicationContext(),
                            LoginActivity.class
                    );
                } else if (response.code() == 409){
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "User already Exits!"
                    );
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Failed to register user!"
                    );

                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed"
                );
            }
        });
    }
}