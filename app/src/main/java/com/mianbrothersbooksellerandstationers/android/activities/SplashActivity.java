package com.mianbrothersbooksellerandstationers.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.admin.AdminDashboardActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long Splash_Time = 3500;
    private ImageView logo;
    private TextView tvAppName;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    boolean isLoggedIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        setAnimationOnView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedIN)
                {
                    startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
                    overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
                    Toast.makeText(SplashActivity.this, "Welcome Back Admin Mian Brothers", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    nextActivity();
                }
            }
        }, Splash_Time);

    }

    private void nextActivity() {
        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        finish();
    }

    private void initView() {
        logo = findViewById(R.id.logo);
        tvAppName = findViewById(R.id.tvAppName);

        sh = getSharedPreferences("DAT", MODE_PRIVATE);
        editor = sh.edit();
        isLoggedIN = sh.getBoolean("IsLOGGEDIN", false);

    }
    private void setAnimationOnView() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_slideup);
        logo.setAnimation(anim);
        tvAppName.setAnimation(slideUpAnimation);
    }
}