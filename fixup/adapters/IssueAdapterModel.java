package com.example.fixup.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.fixup.R;
import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.models.Issue;
import com.example.fixup.models.IssueDetailModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IssueAdapterModel extends ArrayAdapter<Issue> {
    private SessionManagerUtil sessionManagerUtil;
    private Context mContext;
    private int mResource;
    private List<Issue> items;
    public IssueAdapterModel(@NonNull Context context, int resource, @NonNull List<Issue> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.items = objects;
        this.sessionManagerUtil = new SessionManagerUtil(mContext);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Issue issue = items.get(position);
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewArea = convertView.findViewById(R.id.textViewArea);
        TextView textViewMaxVolunteers = convertView.findViewById(R.id.textViewMaxVolunteers);
        TextView textViewCreatedTime = convertView.findViewById(R.id.textViewCreatedTime);
        TextView textViewContent = convertView.findViewById(R.id.textViewContent);
        Button volunteerButton = convertView.findViewById(R.id.volunteerButton);
        textViewTitle.setText(issue.getTitle());
        textViewArea.setText(issue.getArea());
        textViewMaxVolunteers.setText("Volunteers Need: " + String.valueOf(issue.getVolunteersNeeded()));
        textViewCreatedTime.setText(issue.getCreatedAt());
        textViewContent.setText(issue.getDescription());
        String imageUri = issue.getImageFilePath();
        imageUri = imageUri.replace("\\", "/");
        Glide.with(mContext)
                .load("http://192.168.41.156:3000/" + imageUri)
                .override(200, 200)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_error_image)
                .into(imageView);
        volunteerButton.setOnClickListener(v -> {
            String issueId = issue.get_id();
            addVolunteertoIssue(issueId, sessionManagerUtil.getEmail(), volunteerButton);
        });
        return convertView;
    }

    private void addVolunteertoIssue(String issueId, String email, Button volunteerButton) {
        HashMap<String, String> map = new HashMap<>();
        map.put("issueId", issueId);
        map.put("email", email);
        Retrofit retrofit = RetroFitClient.getClient(getContext().getString(R.string.base_url) + "/");
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.addVolunteerToIssue(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful() && response.code() == 201) {
                    volunteerButton.setText("Volunteered");
                    volunteerButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
                    volunteerButton.setEnabled(false);
                    AndroidUtil.showToast(
                            mContext,
                            "Volunteered"
                    );
                } else {
                    AndroidUtil.showToast(
                            mContext,
                            response.message()
                    );
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AndroidUtil.showToast(
                        mContext,
                        "Request failed!"
                );
            }
        });
    }
}
