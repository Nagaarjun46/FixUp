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
import com.example.fixup.apicalls.models.LoginRequestModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, signupButton;
    private SessionManagerUtil sessionManagerUtil;
    private EditText emailText, passwordText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        emailText = findViewById(R.id.user_email);
        passwordText = findViewById(R.id.user_password);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.login_progress_bar);
        sessionManagerUtil = new SessionManagerUtil(this);
        loginButton.setOnClickListener(v -> {
            if (isValid()) {
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                verifyUser(email, password);
            }
        });
        signupButton.setOnClickListener(v -> {
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            AndroidUtil.switchActivity(
                    this,
                    SignUpActivity.class
            );
        });
    }

    private void verifyUser(String email, String password) {
        AndroidUtil.setButtonEnabled(
                loginButton,
                false
        );
        AndroidUtil.setProgressBarVisible(
                progressBar,
                true
        );
        Retrofit retrofit = RetroFitClient.getClient(getString(R.string.base_url));
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", password);
        Call<LoginRequestModel> call = apiService.loginUser(map);
        call.enqueue(new Callback<LoginRequestModel>() {
            @Override
            public void onResponse(Call<LoginRequestModel> call, Response<LoginRequestModel> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if (response.isSuccessful() && response.body() != null) {
                    AndroidUtil.setProgressBarVisible(
                            progressBar,
                            false
                    );
                    AndroidUtil.setButtonEnabled(
                            loginButton,
                            true
                    );
                    String sessionId = response.body().getSessionId();
                    String city = response.body().getUserCity();
                    sessionManagerUtil.createSession(sessionId, email, city);
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Login Successfully!"
                    );
                    AndroidUtil.switchActivity(
                            getApplicationContext(),
                            MainActivity.class
                    );
                } else if (response.code() == 400) {
                    AndroidUtil.setButtonEnabled(
                            loginButton,
                            true
                    );
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "User Not Found."
                    );
                } else {
                    AndroidUtil.setButtonEnabled(
                            loginButton,
                            true
                    );
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Unexpected Error!"
                    );
                }
            }

            @Override
            public void onFailure(Call<LoginRequestModel> call, Throwable t) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.setButtonEnabled(
                        loginButton,
                        true
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed"

                );
            }
        });
    }

    private boolean isValid() {
        if (emailText.getText().toString().trim().isEmpty()) {
            emailText.setError("Email is required");
            return false;
        }
        if (passwordText.getText().toString().trim().isEmpty()) {
            passwordText.setError("Password is required");
            return false;
        }
        return true;
    }
}