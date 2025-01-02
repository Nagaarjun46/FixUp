package com.example.fixup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.fixup.adapters.IssueAdapterModel;
import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.models.Issue;
import com.example.fixup.models.IssueDetailModel;
import com.example.fixup.models.IssueResponse;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private SessionManagerUtil sessionManagerUtil;
    private ListView listView;
    private ProgressBar progressBar;
    private IssueAdapterModel issueAdapterModel;
    public List<Issue> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        items = new ArrayList<>();
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null) {
            AndroidUtil.showToast(
                    this,
                    "Data is null"
            );
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AndroidUtil.showDialog(
                        getApplicationContext(),
                        "Remainder",
                        "Do you want to close the app",
                        "Yes",
                        "No",
                        (dialog, which) -> {
                           moveTaskToBack(true);
                        },
                        (dialog, which) -> {
                            dialog.dismiss();
                        }
                );
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.POST_NOTIFICATIONS },
                    1
            );
        }
        sessionManagerUtil = new SessionManagerUtil(this);
        listView = findViewById(R.id.issue_feed_list);
        progressBar = findViewById(R.id.home_progress_bar);
        AndroidUtil.setProgressBarVisible(
                progressBar,
                true
        );
        fetchIssueByCity();
        issueAdapterModel = new IssueAdapterModel(this, R.layout.layout_issue_adapter_model, items);
        listView.setAdapter(issueAdapterModel);
    }

    private void fetchIssueByCity() {
        Retrofit retrofit = RetroFitClient.getClient(getString(R.string.base_url));
        ApiService apiService = retrofit.create(ApiService.class);
        String city = sessionManagerUtil.getCity();
        HashMap<String, String> map = new HashMap<>();
        map.put("city", city);
        Call<IssueResponse> call = apiService.fetchPostByUserCity(map);
        call.enqueue(new Callback<IssueResponse>() {
            @Override
            public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                if(response.isSuccessful()) {
                    AndroidUtil.setProgressBarVisible(
                            progressBar,
                            false
                    );
                    if(response.body() != null) {
                        items.clear();
                        items.addAll(response.body().getIssues());
                        issueAdapterModel.notifyDataSetChanged();
                    } else {
                        AndroidUtil.showToast(
                                getApplicationContext(),
                                "No issues to show!"
                        );
                    }
                }
            }
            @Override
            public void onFailure(Call<IssueResponse>call, Throwable t) {
                Log.e("API_ERROR", "Request failed: " + t.getMessage());
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Failed to load issues"
                );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.user_profile) {
            AndroidUtil.showToast(
                    this,
                    "Directing to Profile Page"
            );
            AndroidUtil.switchActivity(
                    this,
                    ProfileActivity.class
            );
        } else if (item.getItemId() == R.id.user_notification) {
            AndroidUtil.showToast(
                    this,
                    "Directing to Notifications Page"
            );
            AndroidUtil.switchActivity(
                    this,
                    NotificationActivity.class
            );
        } else if (item.getItemId() == R.id.user_settings) {
            AndroidUtil.showToast(
                    this,
                    "Directing to Settings Page"
            );
            AndroidUtil.switchActivity(
                    this,
                    SettingActivity.class
            );
        } else if (item.getItemId() == R.id.add_posts) {
            AndroidUtil.showToast(
                    this,
                    "Directing to Add Post Page"
            );
            AndroidUtil.switchActivity(
                    this,
                    AddPostActivity.class
            );
        } else if (item.getItemId() == R.id.refresh) {
            AndroidUtil.showToast(
                    this,
                    "Refreshing the Page"
            );
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            fetchIssueByCity();
        } else if (item.getItemId() == R.id.logout) {
            AndroidUtil.showToast(
                    this,
                    "Logging Out"
            );
            AndroidUtil.moveToLoginPage(
                    this,
                    LoginActivity.class
            );
            sessionManagerUtil.logout();
            finish();
        }
        return true;
    }
}