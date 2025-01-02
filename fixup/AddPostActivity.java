package com.example.fixup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.apicalls.models.NotificationUserModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPostActivity extends AppCompatActivity {
    private EditText title, description, priority, city, area, volunteersNeeded;
    private Button uploadButton, addPostButton;
    private ProgressBar progressBar;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private SessionManagerUtil sessionManagerUtil;
    private Uri imageUri;
    private static final String CHANNEL_ID = "issue_notification";
    private static final String CHANNEL_NAME = "Issue Notification";
    private static final String DESCRIPTION = "A channel for sending the issue notifications.";
    private static final int IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.add_post_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AndroidUtil.showDialog(
                        getApplicationContext(),
                        "Remainder",
                        "Do you want to move to previous page",
                        "Yes",
                        "No",
                        (dialog, which) -> {
                            finish();
                        },
                        (dialog, which) -> {
                            dialog.dismiss();
                        }
                );
            }
        });
        sessionManagerUtil = new SessionManagerUtil(this);
        title = findViewById(R.id.post_title);
        description = findViewById(R.id.post_description);
        priority = findViewById(R.id.post_priority);
        city = findViewById(R.id.post_city);
        area = findViewById(R.id.post_area);
        volunteersNeeded = findViewById(R.id.volunteers_needed_count);
        progressBar = findViewById(R.id.add_post_progress_bar);
        uploadButton = findViewById(R.id.upload_image_button);
        addPostButton = findViewById(R.id.add_post_button);
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                    }
                }
        );
        uploadButton.setOnClickListener(v -> {
            pickImageFromDevice();
        });
        addPostButton.setOnClickListener(v -> {
            if(isValid()) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        true
                );
                AndroidUtil.setButtonEnabled(
                        addPostButton,
                        false
                );
                addIssue();
            }
        });
    }

    private boolean isValid() {
        if(title.getText().toString().trim().isEmpty()) {
            title.setError("Title is required");
            return false;
        }
        if(description.getText().toString().trim().isEmpty()) {
            description.setError("Description is required");
            return false;
        }
        if(priority.getText().toString().trim().isEmpty()) {
            priority.setError("Priority is required");
            return false;
        }
        if(city.getText().toString().trim().isEmpty()) {
            city.setError("City is required");
            return false;
        }
        if(area.getText().toString().trim().isEmpty()) {
            area.setError("Area is required");
            return false;
        }
        if(volunteersNeeded.getText().toString().trim().isEmpty()) {
            volunteersNeeded.setError("Number of volunteers needed is required");
            return false;
        }
        if(imageUri == null) {
            uploadButton.setError("Upload a Appropriate image");
            return false;
        }
        return true;
    }

    private void pickImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageLauncher.launch(Intent.createChooser(intent, "Select Issue Image"));
    }
    private void addIssue() {
        String issueTitle = title.getText().toString().trim();
        String issueDescription = description.getText().toString().trim();
        int issuePriority = Integer.parseInt(priority.getText().toString().trim());
        String issueCity = city.getText().toString().trim();
        String issueArea = area.getText().toString().trim();
        int issueVolunteersNeeded = Integer.parseInt(volunteersNeeded.getText().toString().trim());
        Retrofit retrofit = RetroFitClient.getClient(getString(R.string.base_url));
        ApiService apiService = retrofit.create(ApiService.class);
        File file = new File(getRealPathFromUri(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("/image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RequestBody requestTitle = RequestBody.create(MediaType.parse("text/plain"), issueTitle);
        RequestBody requestDescription = RequestBody.create(MediaType.parse("text/plain"), issueDescription);
        RequestBody requestCity = RequestBody.create(MediaType.parse("text/plain"), issueCity);
        RequestBody requestArea = RequestBody.create(MediaType.parse("text/plain"), issueArea);
        RequestBody requestPriority = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(issuePriority));
        RequestBody requestVolunteersNeeded = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(issueVolunteersNeeded));
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), sessionManagerUtil.getEmail());
        Call<Void> call = apiService.addPost(
                requestTitle,
                requestDescription,
                imagePart,
                requestCity,
                requestArea,
                requestPriority,
                requestVolunteersNeeded,
                requestEmail
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("AddPostActivity", "Response: " + response.toString());
                    clearAllEditText();
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Issue added successfully"
                    );
                    sendNotificationToUsersByEmail(
                            issueDescription
                    );
                } else {
                    Log.e("AddPostActivity", "Response failed: " + response.message());

                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Failed to add issue: " + response.message()
                    );
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Failed to add issue. Please try again later."
                );
                Log.e("Add Issue", "Error: ", t);
            }
        });
    }
    private void sendNotificationToUsersByEmail(String issueDescription) {
        Retrofit retrofit = RetroFitClient.getClient(getString(R.string.base_url) + "/");
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("city", sessionManagerUtil.getCity());
        Call<NotificationUserModel> call = apiService.sendNotificationToUsersByEmail(map);
        call.enqueue(new Callback<NotificationUserModel>() {
            @Override
            public void onResponse(Call<NotificationUserModel> call, Response<NotificationUserModel> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.setButtonEnabled(
                        addPostButton,
                        true
                );
                if (response.isSuccessful()) {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Notification Send to the nearest Volunteers."
                    );
                } else {
                    AndroidUtil.showToast(
                            getApplicationContext(),
                            "Error in Sending Notification."
                    );
                    try {
                        int errorCode = response.code();
                        String errorBody = response.errorBody().string();
                        Log.e("NOTIFICATION_ERROR", "Error Code: " + errorCode + " ErrorBody: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<NotificationUserModel> call, Throwable t) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.setButtonEnabled(
                        addPostButton,
                        true
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed"
                );
            }
        });
    }
    private void clearAllEditText() {
        title.setText("");
        description.setText("");
        priority.setText("");
        city.setText("");
        area.setText("");
        volunteersNeeded.setText("");
        imageUri = null;
    }
    private String getRealPathFromUri(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        String path = null;
        if(contentUri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null)) {
                if(cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    path = cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("AddPostActivity", "Error getting real path from URI: ", e);
            }
        } else if (contentUri.getScheme().equals("file")) {
            path = contentUri.getPath();
        }
        return path;
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
}