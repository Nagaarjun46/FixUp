package com.example.fixup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.models.ProfileModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    public static final String USERNAME_REGEX = "^[A-Za-z\\s]{3,30}$";
    public static final String CONTACT_NO_REGEX = "^[6-9]\\d{9}$";
    private SessionManagerUtil sessionManagerUtil;
    private EditText nameText, contactNoText, stateText, cityText;
    private Button edit_name, edit_contact_no, edit_state, edit_city;
    private ProgressBar progressBar;
    private String orgName, orgContactNo, orgState, orgCity;
    private boolean isEditName = false, isEditContactNo = false, isEditState = false, isEditCity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        nameText = findViewById(R.id.profile_user_name);
        contactNoText = findViewById(R.id.profile_user_contact_no);
        stateText = findViewById(R.id.profile_user_state);
        cityText = findViewById(R.id.profile_user_city);
        edit_name = findViewById(R.id.profile_user_name_edit);
        edit_contact_no = findViewById(R.id.profile_user_contact_no_edit);
        edit_state = findViewById(R.id.profile_user_state_edit);
        edit_city = findViewById(R.id.profile_user_city_edit);
        progressBar = findViewById(R.id.profile_progress_bar);
        sessionManagerUtil = new SessionManagerUtil(this);
        fetchUserDetails(sessionManagerUtil.getEmail());
        edit_name.setOnClickListener(v -> {
            if (!isEditName) {
                setAsCommitButton(edit_name);
                setEditableText(nameText);
                isEditName = true;
            } else {
                String name = nameText.getText().toString().trim();
                if (!name.equals(orgName)) {
                    if (name.matches(USERNAME_REGEX)) {
                        AndroidUtil.setProgressBarVisible(
                                progressBar,
                                true
                        );
                        updateUserName(name);
                        isEditName = false;
                    } else {
                        setTextUnChanged(nameText, orgName);
                        AndroidUtil.showToast(
                                this,
                                "Invalid name"
                        );
                    }
                } else {
                    AndroidUtil.showToast(
                            this,
                            "Enter a different name"
                    );
                }
            }

        });
        edit_contact_no.setOnClickListener(v -> {
            if (!isEditContactNo) {
                setAsCommitButton(edit_contact_no);
                setEditableText(contactNoText);
                isEditContactNo = true;
            } else {
                String contactNo = contactNoText.getText().toString().trim();
                if (!contactNo.equals(orgContactNo)) {
                    if (contactNo.matches(CONTACT_NO_REGEX)) {
                        updateUserContactNo(contactNo);
                        isEditContactNo = false;
                    } else {
                        setTextUnChanged(contactNoText, orgContactNo);
                        AndroidUtil.showToast(
                                this,
                                "Invalid Contact Number"
                        );
                    }
                } else {
                    AndroidUtil.showToast(
                            this,
                            "Enter a different phone number"
                    );
                }
            }
        });
        edit_state.setOnClickListener(v -> {
            if (!isEditState) {
                setAsCommitButton(edit_state);
                setEditableText(stateText);
                isEditState = true;
            } else {
                String state = stateText.getText().toString().trim();
                if (!state.equals(orgState)) {
                    if (state.matches(USERNAME_REGEX)) {
                        updateUserState(state);
                    } else {
                        setTextUnChanged(stateText, orgState);
                        AndroidUtil.showToast(
                                this,
                                "Invalid state"
                        );
                    }
                } else {
                    AndroidUtil.showToast(
                            this,
                            "Enter a different state"
                    );
                }
            }
        });
        edit_city.setOnClickListener(v -> {
            if (!isEditCity) {
                setAsCommitButton(edit_city);
                setEditableText(cityText);
                isEditCity = true;
            } else {
                String city = cityText.getText().toString().trim();
                if (!city.equals(orgCity)) {
                    if (city.matches(USERNAME_REGEX)) {
                        updateUserCity(city);
                    } else {
                        setTextUnChanged(cityText, orgCity);
                        AndroidUtil.showToast(
                                this,
                                "Invalid name"
                        );
                    }
                } else {
                    AndroidUtil.showToast(
                            this,
                            "Enter a different name"
                    );
                }
            }
        });
    }

    private void updateUserName(String name) {
        Retrofit retrofit = RetroFitClient.getClient("http://192.168.41.156:3000/api/");
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", sessionManagerUtil.getEmail());
        map.put("name", name);
        Call<Void> call = apiService.updateUserName(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if (response.isSuccessful()) {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Name is changed successfully!"
                    );
                    setBlockedText(nameText);
                    orgName = name;
                    setAsEditButton(edit_city);
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Error in changing name"
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
                        "Request failed!"
                );
                Log.e("UPDATE_DATA", "Error: ", t);
            }
        });
    }
    private void updateUserContactNo(String contactNo) {
        Retrofit retrofit = RetroFitClient.getClient("http://192.168.41.156:3000/");
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", sessionManagerUtil.getEmail());
        map.put("contact_no", contactNo);
        Call<Void> call = apiService.updateUserContactNo(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if (response.isSuccessful()) {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Contact No is changed successfully!"
                    );
                    setBlockedText(contactNoText);
                    orgContactNo = contactNo;
                    setAsEditButton(edit_contact_no);
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Error in changing Contact No"
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
                        "Request failed!"
                );
                Log.e("UPDATE_DATA", "Error: ", t);
            }
        });
    }
    private void updateUserState(String state) {
        Retrofit retrofit = RetroFitClient.getClient("http://192.168.41.156:3000/");
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", sessionManagerUtil.getEmail());
        map.put("state", state);
        Call<Void> call = apiService.updateUserState(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if (response.isSuccessful()) {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "State is changed successfully!"
                    );
                    setBlockedText(stateText);
                    orgState = state;
                    setAsEditButton(edit_state);
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Error in changing state"
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
                        "Request failed!"
                );
                Log.e("UPDATE_DATA", "Error: ", t);
            }
        });
    }
    private void updateUserCity(String city) {
        Retrofit retrofit = RetroFitClient.getClient("http://192.168.41.156:3000/");
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map  = new HashMap<>();
        map.put("email", sessionManagerUtil.getEmail());
        map.put("city", city);
        Call<Void> call = apiService.updateUserCity(map);
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
                            "City was updated successfully."
                    );
                    setBlockedText(cityText);
                    orgCity = city;
                    setAsEditButton(edit_city);
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Error in changing name"
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
                        "Request failed!"
                );
                Log.e("UPDATE_DATA", "Error: ", t);
            }
        });
    }

    private void fetchUserDetails(String email) {
        Retrofit retrofit = RetroFitClient.getClient(this.getString(R.string.base_url));
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map  = new HashMap<>();
        map.put("email", email);
        Call<ProfileModel> call = apiService.fetchUserDetailsForProfile(map);
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.d("Full Response", new Gson().toJson(response.body()));
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "User Details Fetched"
                    );
                    orgName = response.body().getName();
                    if (orgName != null) {
                        Log.d("Profile Data", orgName);
                        nameText.setText(orgName);
                    }

                    orgContactNo = response.body().getContactNo();
                    if (orgContactNo != null) {
                        Log.d("Profile Data", orgContactNo);
                        contactNoText.setText(orgContactNo);
                    }

                    orgState = response.body().getState();
                    if (orgState != null) {
                        Log.d("Profile Data", orgState);
                        stateText.setText(orgState);
                    }

                    orgCity = response.body().getCity();
                    if (orgCity != null) {
                        Log.d("Profile Data", orgCity);
                        cityText.setText(orgCity);
                    }

                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            response.message()
                    );
                }
            }
            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed"
                );
            }
        });
    }
    private void setTextUnChanged(EditText editText, String text) {
        editText.setText(text);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.home_icon) {
            AndroidUtil.showToast(
                    this,
                    "Redirecting to Home Page"
            );
            AndroidUtil.switchActivity(
                    this,
                    MainActivity.class
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setAsCommitButton(Button button) {
        button.setText("Commit");
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_commit_button));
    }
    private void setAsEditButton(Button button) {
        button.setText("Edit");
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_button_background));
    }
    private void setEditableText(EditText editText) {
        editText.setEnabled(true);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_edittext_background));
    }
    private void setBlockedText(EditText editText) {
        editText.setEnabled(false);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_block_edittext_background));
    }
}