package com.example.madapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "Starting SplashActivity onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Creating intent for MainActivity");
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        Log.d(TAG, "Starting MainActivity");
                        startActivity(intent);
                        Log.d(TAG, "Finishing SplashActivity");
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error transitioning to MainActivity", e);
                        Toast.makeText(SplashActivity.this, 
                            "Error starting app: " + e.getMessage(), 
                            Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }, SPLASH_DELAY);
            
            Log.d(TAG, "SplashActivity onCreate completed");
        } catch (Exception e) {
            Log.e(TAG, "Error in SplashActivity onCreate", e);
            Toast.makeText(this, 
                "Error starting app: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
            finish();
        }
    }
} 