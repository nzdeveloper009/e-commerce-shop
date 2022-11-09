package com.mianbrothersbooksellerandstationers.android.activities.admin;

import static com.mianbrothersbooksellerandstationers.android.firebase.RealTimeClass.getCategories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mianbrothersbooksellerandstationers.android.R;

public class ExistProductActivity extends AppCompatActivity {

    RecyclerView categoryPRv,productPRv;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_product);
        categoryPRv = findViewById(R.id.categoryPRv);
        productPRv = findViewById(R.id.productPRv);
        getCategories(ExistProductActivity.this,categoryPRv,productPRv, false);
    }
}