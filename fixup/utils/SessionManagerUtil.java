package com.example.fixup.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.EnumMap;

public class SessionManagerUtil {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CITY = "city";
    public SessionManagerUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void createSession(String sessionId, String email, String city) {
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CITY, city);
        editor.apply();
    }
    public String getSessionId() {
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }
    public String getCity() {
        return sharedPreferences.getString(KEY_CITY, null);
    }
    public boolean isLoggedIn() {
        return getSessionId() != null;
    }
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
