
package com.example.fixup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.fixup.utils.AndroidUtil;
import com.example.fixup.utils.SessionManagerUtil;

public class SplashActivity extends AppCompatActivity {
    private SessionManagerUtil sessionManagerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManagerUtil = new SessionManagerUtil(this);
        if(!sessionManagerUtil.isLoggedIn()) {
            AndroidUtil.splashSwitchActivity(
                    this,
                    LoginActivity.class
            );
        } else {
            AndroidUtil.splashSwitchActivity(
                    this,
                    MainActivity.class
            );
        }
    }
}