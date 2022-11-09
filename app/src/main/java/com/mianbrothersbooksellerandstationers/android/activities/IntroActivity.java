package com.mianbrothersbooksellerandstationers.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.mianbrothersbooksellerandstationers.android.R;

public class IntroActivity extends AppCompatActivity {

    private AppCompatButton btnSignInIntro, btnSignUpIntro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initView();
        listeners();
    }

    private void listeners() {
        btnSignInIntro.setOnClickListener(it -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        });
        btnSignUpIntro.setOnClickListener(it -> {
            startActivity(new Intent(IntroActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.anim_slideup, R.anim.anim_slidebottom);
        });
    }

    private void initView() {
        btnSignInIntro = findViewById(R.id.btn_sign_in_intro);
        btnSignUpIntro = findViewById(R.id.btn_sign_up_intro);
    }
}