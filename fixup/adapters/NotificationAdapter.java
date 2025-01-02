package com.example.fixup.adapters;

import static android.provider.Settings.System.getString;

import android.app.appsearch.SearchSuggestionSpec;
import android.content.Context;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.fixup.R;
import com.example.fixup.apicalls.ApiService;
import com.example.fixup.apicalls.RetroFitClient;
import com.example.fixup.models.NotificationModel;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {
    private Context mContext;
    private int mResource;
    private List<NotificationModel> items;
    private SessionManagerUtil sessionManagerUtil;
    public NotificationAdapter(Context context, int resource, List<NotificationModel> items) {
        super(context, resource, items);
        this.mContext = context;
        this.mResource = resource;
        this.items = items;
        this.sessionManagerUtil = new SessionManagerUtil(mContext);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationModel notificationModel = items.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }
        View container = convertView.findViewById(R.id.notification_container);
        TextView notificationTitle = convertView.findViewById(R.id.notification_title);
        TextView notificationCreatedAt = convertView.findViewById(R.id.notification_created_at);
        TextView notificationContent = convertView.findViewById(R.id.notification_content);
        notificationTitle.setText(notificationModel.getTitle());
        notificationContent.setText(notificationModel.getContent());
        notificationCreatedAt.setText(notificationModel.getCreatedAt());
        if (!notificationModel.isSeen()) {
            container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.custom_block_edittext_background));
        }
        notificationTitle.setOnClickListener(v -> {
            if (!notificationModel.isSeen()) {
                container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.custom_edittext_background));
                notificationModel.setSeen(true);
            }
        });
        notificationContent.setOnClickListener(v -> {
            if (!notificationModel.isSeen()) {
                container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.custom_edittext_background));
                notificationModel.setSeen(true);
            }
        });
        notificationCreatedAt.setOnClickListener(v -> {
            if (!notificationModel.isSeen()) {
                container.setBackground(ContextCompat.getDrawable(mContext, R.drawable.custom_edittext_background));
                notificationModel.setSeen(true);
            }
        });
        return convertView;
    }
}
