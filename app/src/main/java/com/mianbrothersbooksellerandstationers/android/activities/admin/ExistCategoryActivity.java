package com.mianbrothersbooksellerandstationers.android.activities.admin;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCategories;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.mianbrothersbooksellerandstationers.android.R;
import com.mianbrothersbooksellerandstationers.android.activities.BaseActivity;

public class ExistCategoryActivity extends BaseActivity {

    RecyclerView categoryRv,productRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_exist_category);

        categoryRv = findViewById(R.id.categoryRv);
        productRv = findViewById(R.id.productRv);
        getCategories(ExistCategoryActivity.this,categoryRv,productRv, false);

    }

}