package com.example.fixup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.fixup.adapters.NotificationAdapter;
import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.apicalls.models.NotificationResponse;
import com.example.fixup.models.NotificationModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationActivity extends AppCompatActivity {
    private SessionManagerUtil sessionManagerUtil;
    private ListView listView;
    private NotificationAdapter adapter;
    private List<NotificationModel> items;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sessionManagerUtil = new SessionManagerUtil(this);
        progressBar = findViewById(R.id.notification_progress_bar);
        items = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        AndroidUtil.setProgressBarVisible(
                progressBar,
                true
        );
        fetchNotifications();
        listView = findViewById(R.id.notification_list_view);
        adapter = new NotificationAdapter(this, R.layout.layout_issue_notification_adapter, items);
        listView.setAdapter(adapter);
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
    }

    private void fetchNotifications() {
        Retrofit retrofit = RetroFitClient.getClient(getString(R.string.base_url));
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", sessionManagerUtil.getEmail());
        Call<NotificationResponse> call = apiService.fetchNotifications(map);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();
                    items.addAll(response.body().getNotifications());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                AndroidUtil.setProgressBarVisible(
                        progressBar,
                        false
                );
                AndroidUtil.showToast(
                        getApplicationContext(),
                        "Request Failed."
                );
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu, menu);
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
        } else if (item.getItemId() == R.id.refresh_notifications) {
            AndroidUtil.showToast(
                    this,
                    "Reloading notifications"
            );
            AndroidUtil.setProgressBarVisible(
                    progressBar,
                    true
            );
            fetchNotifications();
        }
        return super.onOptionsItemSelected(item);
    }
}